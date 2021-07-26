package mk.ukim.finki.eshop.ui.account.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.api.dto.LoginDto
import mk.ukim.finki.eshop.databinding.FragmentLoginBinding
import mk.ukim.finki.eshop.ui.account.AccountViewModel
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()

    private val validator = LoginFormValidator()

    var username = ""
    var password = ""

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.setLifecycleOwner(this)
        binding.validator = validator

        setupObserverForIfUserExists()
        setupLoginObserver()

        binding.loginBtn.setOnClickListener {
            username = binding.usernameEditText.text.toString()
            password = binding.passwordEditText.text.toString()

            validator.usernameLiveData.value = username
            validator.passwordLiveData.value = password

            if (validator.validateForm()) {
                accountViewModel.checkIfUsernameAlreadyExists(username, "login")
            }
        }
        return binding.root
    }

    private fun setupLoginObserver() {
        accountViewModel.authResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Error
                && response.message!!.contains("password")) {
                Utils.showToast(
                    requireContext(),
                    response.message,
                    Toast.LENGTH_LONG
                )
            }
        })
    }

    private fun setupObserverForIfUserExists() {
        accountViewModel.userExistsResponseLogin.observe(viewLifecycleOwner,{ response ->
            when(response) {
                is NetworkResult.Success -> {
                    if (response.data!!)
                        accountViewModel.login(LoginDto(username, password))
                    else {
                        Utils.showToast(
                            requireContext(),
                            "The data you provided is not correct...",
                            Toast.LENGTH_LONG
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