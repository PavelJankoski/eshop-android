package mk.ukim.finki.eshop.ui.account.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.dto.RegisterDto
import mk.ukim.finki.eshop.databinding.FragmentProfileBinding
import mk.ukim.finki.eshop.databinding.FragmentRegisterBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.logoutBtn.setOnClickListener {
            loginManager.logoutUser()
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}