package mk.ukim.finki.eshop.ui.account.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.dto.LoginDto
import mk.ukim.finki.eshop.databinding.FragmentAccountBinding
import mk.ukim.finki.eshop.databinding.FragmentLoginBinding
import mk.ukim.finki.eshop.ui.account.AccountViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginBtn.setOnClickListener {
            accountViewModel.login(LoginDto(binding.usernameEditText.text.toString(), binding.passwordEditText.text.toString()))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}