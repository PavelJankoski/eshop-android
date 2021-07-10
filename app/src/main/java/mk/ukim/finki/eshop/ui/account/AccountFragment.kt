package mk.ukim.finki.eshop.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.PagerAdapter
import mk.ukim.finki.eshop.api.dto.TokenDto
import mk.ukim.finki.eshop.databinding.FragmentAccountBinding
import mk.ukim.finki.eshop.ui.account.login.LoginFragment
import mk.ukim.finki.eshop.ui.account.register.RegisterFragment
import java.util.*

@AndroidEntryPoint
class AccountFragment : Fragment() {
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

        setupViewPager()
        return binding.root
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
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}