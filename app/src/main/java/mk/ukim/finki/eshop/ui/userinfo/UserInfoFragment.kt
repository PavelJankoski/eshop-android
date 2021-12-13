package mk.ukim.finki.eshop.ui.userinfo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentUserInfoBinding
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.ui.account.register.RegisterFormValidator
import mk.ukim.finki.eshop.ui.products.ProductsFragmentArgs
import mk.ukim.finki.eshop.ui.products.ProductsViewModel
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UserInfoFragmentArgs>()
    private val userInfoViewModel by viewModels<UserInfoViewModel>()
    private val validator = UserInfoFormValidator()
    private var imageUrl: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.validator = validator
        binding.user = args.user
        setupTextFields()
        observeUpdateUserInfo()
        binding.profilePictureCardView.setOnClickListener {
            if (hasPermissions(requireContext(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                choosePictureFromGallery()
            } else {
                permReqLauncher.launch(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )
            }
        }
        binding.updateButton.setOnClickListener {
            updateInfo()
        }
        return binding.root
    }

    private fun setupTextFields() {
        validator.nameLiveData.value = args.user.name
        validator.surnameLiveData.value = args.user.surname
        validator.phoneNumberLiveData.value = args.user.phoneNumber
    }

    private fun observeUpdateUserInfo() {
        userInfoViewModel.updateUserResponse.observe(viewLifecycleOwner, {response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.updateButton.isEnabled = true
                    Utils.showToast(requireContext(), "Successfully updated info!", Toast.LENGTH_LONG)
                }
                is NetworkResult.Error -> {
                    binding.updateButton.isEnabled = true
                    Utils.showToast(requireContext(), "Error updating your info, please try again later", Toast.LENGTH_LONG)
                }
                else -> {
                    binding.updateButton.isEnabled = false
                }
            }
        })
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Uri = result.data?.data!!
            imageUrl = data
            binding.profilePictureImageView.setImageURI(data)
        }
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                choosePictureFromGallery()
            }
            else {
                Utils.showToast(requireContext(), "Cannot choose images without write permission", Toast.LENGTH_LONG)
            }
        }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun choosePictureFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun updateInfo() {
        val name = binding.nameEditText.text.toString()
        val surname = binding.surnameEditText.text.toString()
        val phoneNumber = binding.phoneNumberEditText.text.toString()

        validator.nameLiveData.value = name
        validator.surnameLiveData.value = surname
        validator.phoneNumberLiveData.value = phoneNumber
        if(validator.validateForm()) {
            val filePath = Utils.getRealPathFromURI(requireContext(), imageUrl)
            userInfoViewModel.updateUserInfo(
                args.user.id,
                filePath,
                name,
                surname,
                phoneNumber
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}