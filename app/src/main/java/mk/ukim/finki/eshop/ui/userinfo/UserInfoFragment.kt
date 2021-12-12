package mk.ukim.finki.eshop.ui.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentUserInfoBinding
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.ui.account.register.RegisterFormValidator
import mk.ukim.finki.eshop.ui.products.ProductsFragmentArgs

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UserInfoFragmentArgs>()
    private val validator = UserInfoFormValidator()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.validator = validator
        binding.user = args.user
        setupTextFields();
        return binding.root
    }

    private fun setupTextFields() {
        validator.nameLiveData.value = args.user.name
        validator.surnameLiveData.value = args.user.surname
        validator.phoneNumberLiveData.value = args.user.phoneNumber
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}