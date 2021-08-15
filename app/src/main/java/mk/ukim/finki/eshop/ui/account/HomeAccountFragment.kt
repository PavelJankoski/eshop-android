package mk.ukim.finki.eshop.ui.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
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
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.ui.account.profile.ProfileFragment
import javax.inject.Inject

@AndroidEntryPoint
class HomeAccountFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState == null) {
            setupLoginListener()
            setupInterface()
        }
        return inflater.inflate(R.layout.fragment_home_account, container, false)
    }

    private fun setupInterface() {
        val isLoggedIn = loginManager.loggedIn
        lifecycleScope.launchWhenResumed {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                if (isLoggedIn.value)
                    replace<ProfileFragment>(R.id.account_container_view)
                else
                    replace<AccountFragment>(R.id.account_container_view)
            }
        }
    }

    private fun setupLoginListener() {
        CoroutineScope(Dispatchers.IO).launch {
            loginManager.loggedIn.collect { value ->
                withContext(Main) {
                    setupInterface()
                }
            }
        }
    }
}