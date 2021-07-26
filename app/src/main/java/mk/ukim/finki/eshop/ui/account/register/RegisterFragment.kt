package mk.ukim.finki.eshop.ui.account.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.dto.LoginDto
import mk.ukim.finki.eshop.api.dto.RegisterDto
import mk.ukim.finki.eshop.databinding.FragmentLoginBinding
import mk.ukim.finki.eshop.databinding.FragmentRegisterBinding
import mk.ukim.finki.eshop.ui.account.AccountViewModel
import mk.ukim.finki.eshop.ui.account.login.LoginFormValidator
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()

    private val validator = RegisterFormValidator()

    var name = ""
    var surname = ""
    var username = ""
    var email = ""
    var password = ""

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
        binding.setLifecycleOwner(this)

        setupObserverForIfUserExists()
        setupDefaultRegisterConfiguration()

        binding.registerBtn.setOnClickListener {
            name = binding.nameEditText.text.toString()
            surname = binding.surnameEditText.text.toString()
            username = binding.usernameEditText.text.toString()
            email = binding.emailEditText.text.toString()
            password = binding.passwordEditText.text.toString()

            validator.nameLiveData.value = name
            validator.surnameLiveData.value = surname
            validator.usernameLiveData.value = username
            validator.emailLiveData.value  = email
            validator.passwordLiveData.value = password

            if (validator.validateForm()) {
                accountViewModel.checkIfUsernameAlreadyExists(username, "register")
            }
        }
        return binding.root
    }

    private fun setupDefaultRegisterConfiguration() {
        accountViewModel.registerResponse.value = NetworkResult.Error("null")
    }

    private fun setupObserverForIfUserExists() {
        accountViewModel.userExistsResponseRegister.observe(viewLifecycleOwner,{ response ->
            when(response) {
                is NetworkResult.Success -> {
                    if (!response.data!!) {
                        accountViewModel.registerUser(
                            RegisterDto(name, surname, email, username, password),
                        )
                    } else {
                        Utils.showToast(
                            requireContext(),
                            "User with that username already exists...",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
                is NetworkResult.Error -> {
                    Utils.showToast(requireContext(), response.message!!, Toast.LENGTH_SHORT)
                }
                is NetworkResult.Loading -> {
                    Utils.showToast(
                        requireContext(),
                        "Validation of data in progress....",
                        Toast.LENGTH_SHORT
                    )
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}