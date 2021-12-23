package mk.ukim.finki.eshop.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.BuildConfig
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.PagerAdapter
import mk.ukim.finki.eshop.api.dto.request.TokenDto
import mk.ukim.finki.eshop.databinding.FragmentAccountBinding
import mk.ukim.finki.eshop.ui.account.login.LoginFragment
import mk.ukim.finki.eshop.ui.account.register.RegisterFragment
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils


@AndroidEntryPoint
class AccountFragment : Fragment() {
    private val RC_SIGN_IN = 1
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        callbackManager =  CallbackManager.Factory.create()
        binding.loginButton.fragment = this
        binding.loginButton.setPermissions(
            listOf("public_profile, email")
        )

        setupRegistrationObserver()

        binding.loginButton
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.i("login", "success")
                    accountViewModel.loginWithFacebook(TokenDto(loginResult?.accessToken?.token!!))
                }

                override fun onCancel() {
                    Log.i("login", "cancel")
                }

                override fun onError(e: FacebookException) {
                    Log.i("login", "error")
                }
            })

        setupGoogleButton()
        setupViewPager()
        return binding.root
    }

    private fun setupGoogleButton() {
        val googleButton = binding.signInButton
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        accountViewModel.setGoogleClient(googleSignInClient)
        (googleButton.getChildAt(0) as TextView).text = "Sign in with Google"
        (googleButton.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        googleButton.setOnClickListener {

            val signInIntent: Intent = googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken
            accountViewModel.loginWithGoogle(TokenDto(idToken!!))
        } catch (e: ApiException) {
            Utils.showToast(
                requireContext(),
                "Something went wrong, try again later...",
                Toast.LENGTH_SHORT
            )
        }
    }

    private fun setupRegistrationObserver() {
        accountViewModel.registerResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Success){
                binding.accountViewPager.setCurrentItem(0, true)
                Utils.showToast(requireContext(), "Successfully registered. Please log in", Toast.LENGTH_LONG)
            }
            else {
                Utils.showToast(requireContext(), response.message!!, Toast.LENGTH_LONG)
            }

        })
    }

    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(LoginFragment(), RegisterFragment())
        val tabLayoutTitles = arrayListOf<String>("Log in", "Register")
        val pagerAdapter = PagerAdapter(fragments, this)
        binding.accountViewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.accountTabLayout, binding.accountViewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}