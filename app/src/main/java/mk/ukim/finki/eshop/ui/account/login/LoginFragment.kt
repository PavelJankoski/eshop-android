package mk.ukim.finki.eshop.ui.account.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.validator = validator

        binding.loginBtn.setOnClickListener {
            login()
        }
        accountViewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    binding.alertTextView.text = response.message!!
                    binding.alertConstraintLayout.visibility = View.VISIBLE
                    accountViewModel.logout()
                }
                else -> {
                    Utils.showSnackbar(
                        binding.root,
                        "Successfully logged in!",
                        Snackbar.LENGTH_SHORT
                    )
                    binding.alertConstraintLayout.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    private fun login() {
        validator.emailLiveData.value = binding.emailEditText.text.toString()
        validator.passwordLiveData.value = binding.passwordEditText.text.toString()
        if (validator.validateForm()) {
            accountViewModel.login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}