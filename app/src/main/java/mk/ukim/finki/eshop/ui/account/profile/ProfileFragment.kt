package mk.ukim.finki.eshop.ui.account.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.api.model.User
import mk.ukim.finki.eshop.databinding.FragmentProfileBinding
import mk.ukim.finki.eshop.ui.account.AccountViewModel
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager
    private val accountViewModel: AccountViewModel by activityViewModels()

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
        return binding.root
    }

    private fun observeUserData() {
        accountViewModel.loginResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Error) {
                Utils.showToast(requireContext(), "There seems to be a problem. Try again later", Toast.LENGTH_SHORT)
            } else if (response is NetworkResult.Success) {
                response.data?.let {
                    binding.user = User(response.data.email, response.data.userId, response.data.imageUrl, response.data.fullName.split(" ")[0], response.data.fullName.split(" ")[1])
                }
            } else {
                Log.i("ProfileFragment: observeUserData", "Loading user data")
            }
        })
    }

    fun onOrderHistoryClick() {
        Log.i("sd", "asdsd");
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}