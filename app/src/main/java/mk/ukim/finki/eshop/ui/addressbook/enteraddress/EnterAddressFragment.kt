package mk.ukim.finki.eshop.ui.addressbook.enteraddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.api.dto.request.CreateEditAddressDto
import mk.ukim.finki.eshop.databinding.FragmentEnterAddressBinding
import mk.ukim.finki.eshop.ui.addressbook.AddressBookViewModel
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class EnterAddressFragment : Fragment() {
    private var _binding: FragmentEnterAddressBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EnterAddressFragmentArgs>()
    private val validator = EnterAddressFormValidator()
    private val addressBookViewModel by viewModels<AddressBookViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterAddressBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.validator = validator
        if(args.address != null) {
            setupInputFields()
        }
        binding.saveButton.setOnClickListener {
            handleSaveButton()
        }
        observeCreateEditResponse()
        return binding.root
    }

    private fun observeCreateEditResponse() {
        addressBookViewModel.createEditAddressResponse.observe(viewLifecycleOwner, {
            when(it) {
                is NetworkResult.Success -> {
                    Utils.showSnackbar(binding.root, "Successfully saved address.", Snackbar.LENGTH_SHORT)
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(binding.root, it.message!!, Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        })
    }

    private fun setupInputFields() {
        validator.streetLiveData.value = args.address?.street ?: ""
        validator.streetNumberLiveData.value = args.address?.streetNo ?: ""
        validator.cityLiveData.value = args.address?.city ?: ""
        validator.countryLiveData.value = args.address?.country ?: ""
        validator.postalCodeLiveData.value = args.address?.postalCode.toString()
        validator.isDefaultLiveData.value = args.address?.isDefault
        if(args.address?.isDefault == true) {
            binding.makeDefaultTextView.visibility = View.GONE
            binding.defaultSwitch.visibility = View.GONE
        }
    }

    private fun handleSaveButton() {
        validator.streetLiveData.value = binding.streetEditText.text.toString()
        validator.streetNumberLiveData.value = binding.streetNoEditText.text.toString()
        validator.cityLiveData.value = binding.cityEditText.text.toString()
        validator.countryLiveData.value = binding.countryEditText.text.toString()
        validator.postalCodeLiveData.value = binding.postalCodeEditText.text.toString()
        validator.isDefaultLiveData.value = binding.defaultSwitch.isChecked
        if(validator.validateForm()) {
            val body = CreateEditAddressDto(
                validator.streetLiveData.value!!,
                validator.streetNumberLiveData.value!!,
                validator.cityLiveData.value!!,
                validator.countryLiveData.value!!,
                validator.postalCodeLiveData.value!!.toInt(),
                validator.isDefaultLiveData.value!!
            )
            if(args.address != null) {
                addressBookViewModel.editAddressesForUser(args.address!!.id, body)
            }
            else{
                addressBookViewModel.createAddressesForUser(body)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}