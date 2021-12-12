package mk.ukim.finki.eshop.ui.account.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.User
import mk.ukim.finki.eshop.databinding.FragmentProfileBinding
import mk.ukim.finki.eshop.ui.account.AccountFragment
import mk.ukim.finki.eshop.ui.account.AccountViewModel
import mk.ukim.finki.eshop.ui.account.HomeAccountFragmentDirections
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.userinfo.UserInfoFragment
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import java.util.concurrent.Executor
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        accountViewModel.checkLoggedInUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.listener = this
        observeUserData();
        accountViewModel.getUserInfo();
        setupBiometricPrompt()
        return binding.root
    }

    private fun observeUserData() {
        accountViewModel.userInfoResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Error) {
                Utils.showToast(requireContext(), "There seems to be a problem. Try again later", Toast.LENGTH_SHORT)
            } else if (response is NetworkResult.Success) {
                response.data?.let {
                    binding.user = response.data
                }
            } else {
                Log.i("ProfileFragment: observeUserData", "Loading user data")
            }
        })
    }

    private fun setupBiometricPrompt() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Utils.showToast(requireContext(), "Authentication Error: $errString", Toast.LENGTH_SHORT)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Utils.showToast(requireContext(), "Authentication Failed!", Toast.LENGTH_SHORT)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Utils.showToast(requireContext(), "Authentication Success!", Toast.LENGTH_SHORT)
            }
        })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login to see payment methods")
            .setNegativeButtonText("Cancel")
            .build()
    }

    fun onOrderHistoryClick() {
        Log.i("sd", "asdsd");
    }

    fun onMyDetailsClick() {
        findNavController().navigate(HomeAccountFragmentDirections.actionHomeAccountFragmentToUserInfoFragment3(binding.user!!))
//        parentFragmentManager.commit {
//            setReorderingAllowed(true)
//            replace<UserInfoFragment>(R.id.account_container_view)
//            addToBackStack("user-info")
//        }
    }

    fun onPaymentMethodsClick() {
        biometricPrompt.authenticate(promptInfo)
    }

    fun onLogoutClick() {
        accountViewModel.logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}