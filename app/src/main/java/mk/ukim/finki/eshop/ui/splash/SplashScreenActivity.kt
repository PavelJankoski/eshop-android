package mk.ukim.finki.eshop.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.ActivitySplashScreenBinding
import mk.ukim.finki.eshop.ui.MainActivity
import mk.ukim.finki.eshop.ui.shoppingbag.ShoppingBagViewModel

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    private val shoppingBagManager by viewModels<ShoppingBagViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashScreenStyle)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        shoppingBagManager.getItemsInBagForUser()
        /*binding.splashScreenTextView.startAnimation(AnimationUtils.loadAnimation(this,
            R.anim.fade_animation
        ))*/
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}