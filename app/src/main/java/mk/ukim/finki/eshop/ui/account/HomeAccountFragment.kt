package mk.ukim.finki.eshop.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentHomeAccountBinding
import mk.ukim.finki.eshop.ui.account.profile.ProfileFragment
import javax.inject.Inject

@AndroidEntryPoint
class HomeAccountFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager
    private var _binding: FragmentHomeAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeAccountBinding.inflate(inflater, container, false)
        setupLoginListener()

        return binding.root
    }

    private fun setupInterface() {
        val isLoggedIn = MyApplication.loggedIn
        lifecycleScope.launchWhenResumed {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                if (isLoggedIn.value) {
                    replace<ProfileFragment>(R.id.account_container_view)
                    addToBackStack(null)
                }
                else {
                    replace<AccountFragment>(R.id.account_container_view)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun setupLoginListener() {
        CoroutineScope(Dispatchers.IO).launch {
            MyApplication.loggedIn.collect { _ ->
                withContext(Main) {
                    setupInterface()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}