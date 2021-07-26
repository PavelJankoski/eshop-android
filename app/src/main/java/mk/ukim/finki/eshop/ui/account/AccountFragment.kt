package mk.ukim.finki.eshop.ui.account

import android.R
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.PagerAdapter
import mk.ukim.finki.eshop.api.dto.TokenDto
import mk.ukim.finki.eshop.databinding.FragmentAccountBinding
import mk.ukim.finki.eshop.ui.account.login.LoginFragment
import mk.ukim.finki.eshop.ui.account.register.RegisterFragment
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_SIGN_IN_ID
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils


@AndroidEntryPoint
class AccountFragment : Fragment() {
    private val RC_SIGN_IN = 1
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        callbackManager =  CallbackManager.Factory.create();
        binding.loginButton.fragment = this
        binding.loginButton.setPermissions(
            listOf("public_profile, email")
        )

        setupRegistrationObserver()

        binding.loginButton
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    Log.i("login", "success")
                    accountViewModel.loginWithFacebook(TokenDto(loginResult?.accessToken?.token))
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
        googleButton.setOnClickListener {

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("647139029513-2ubuvb343grcpcmi6m4s91q3dohlo5ah.apps.googleusercontent.com")
                .requestEmail()
                .build()

            val googleApiClient = GoogleSignIn.getClient( activity, gso)
            accountViewModel.setGoogleClient(googleApiClient)

            val signInIntent: Intent = googleApiClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        for (i in 0 until googleButton.childCount) {
            val v = googleButton.getChildAt(i)
            if (v is TextView) {
                val tv = v
                tv.textSize = 14f
                tv.setTypeface(null, Typeface.NORMAL)
                tv.text = "Sign in with Google"
                tv.setTextColor(resources.getColor(R.color.black))
                tv.isSingleLine = true
                tv.setPadding(15, 15, 15, 15)
                val params = tv.getLayoutParams()
                params.height = 70
                tv.setLayoutParams(params)
                return
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken
            accountViewModel.loginWithGoogle(TokenDto(idToken))
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
            if (response is NetworkResult.Success)
                binding.accountViewPager.setCurrentItem(0, true)
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

    private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
        view.post {
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                view.width, View.MeasureSpec.EXACTLY
            )
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                0, View.MeasureSpec.UNSPECIFIED
            )
            view.measure(widthMeasureSpec, heightMeasureSpec)
            if (pager.layoutParams.height != view.measuredHeight) {
                pager.layoutParams = (pager.layoutParams).also {
                    it.height = view.measuredHeight
                }
            }
        }
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