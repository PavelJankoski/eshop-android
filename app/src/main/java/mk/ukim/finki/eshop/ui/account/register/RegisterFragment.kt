package mk.ukim.finki.eshop.ui.account.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.api.dto.request.RegisterDto
import mk.ukim.finki.eshop.databinding.FragmentRegisterBinding
import mk.ukim.finki.eshop.ui.account.AccountViewModel

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()
    private val validator = RegisterFormValidator()

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.validator = validator
        binding.lifecycleOwner = this

        binding.registerBtn.setOnClickListener {
            register()
        }
        return binding.root
    }

    fun register() {
        val name = binding.nameEditText.text.toString()
        val surname = binding.surnameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val phoneNumber = binding.phoneNumberEditText.text.toString()

        validator.nameLiveData.value = name
        validator.surnameLiveData.value = surname
        validator.emailLiveData.value = email
        validator.passwordLiveData.value = password
        if (validator.validateForm()) {
            val dto = RegisterDto(name, surname, email, phoneNumber, password)
            accountViewModel.registerUser(dto)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}