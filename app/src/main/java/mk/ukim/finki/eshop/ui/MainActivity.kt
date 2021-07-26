package mk.ukim.finki.eshop.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.ActivityMainBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.Constants.Companion.LOGIN_STATE
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var loginManager: LoginManager

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        loginManager.checkAndUpdateLoginState()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupBottomNavigation()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val loggedIn = savedInstanceState.getBoolean(LOGIN_STATE, false)
        loginManager.loggedIn.value = loggedIn
        loginManager.checkAndUpdateLoginState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(LOGIN_STATE, loginManager.loggedIn.value)
        super.onSaveInstanceState(outState)
    }

    private fun setupBottomNavigation() {
        navController = findNavController(R.id.navHostFragment)
        val appBarConfig = AppBarConfiguration(setOf(
            /*R.id.homeFragment,*/
            R.id.categoriesFragment,
            R.id.wishlistFragment,
            R.id.homeAccountFragment
        ))
        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}