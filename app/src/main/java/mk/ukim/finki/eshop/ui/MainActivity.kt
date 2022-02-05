package mk.ukim.finki.eshop.ui

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.ActivityMainBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.Constants.Companion.LOGIN_STATE
import mk.ukim.finki.eshop.util.Utils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginManager: LoginManager

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        loginManager.updateAuthState()
        observeLoggedIn()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupBottomNavigation()
    }

    private fun observeLoggedIn() {
        CoroutineScope(Dispatchers.IO).launch {
            MyApplication.loggedIn.collect {
                withContext(Dispatchers.Main) {
                    if (!MyApplication.firstRender) {
                        if (!it) {
                            Utils.showSnackbar(
                                binding.root,
                                "User logged out",
                                Snackbar.LENGTH_SHORT
                            )
                            loginManager.logoutUser()
                        }
                    } else {
                        MyApplication.firstRender = false
                    }
                }
            }
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val loggedIn = savedInstanceState.getBoolean(LOGIN_STATE, false)
        MyApplication.loggedIn.value = loggedIn
        loginManager.updateAuthState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(LOGIN_STATE, MyApplication.loggedIn.value)
        super.onSaveInstanceState(outState)
    }

    private fun setupBottomNavigation() {
        navController = findNavController(R.id.navHostFragment)
        val appBarConfig = AppBarConfiguration(
            setOf(
                /*R.id.homeFragment,*/
                R.id.categoriesFragment,
                R.id.wishlistFragment,
                R.id.homeAccountFragment
            )
        )
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        val menu = popupMenu.menu
        binding.bottomNavigationView.setupWithNavController(menu, navController)
        setupActionBarWithNavController(navController, appBarConfig)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.shoppingBagFragment || destination.id == R.id.checkoutFragment) {
                binding.bottomNavigationView.visibility = View.GONE
                binding.borderBlackView.visibility = View.GONE
            } else if (destination.id == R.id.detailsFragment || destination.id == R.id.searchFragment || destination.id == R.id.qrCodeFragment) {
                binding.bottomNavigationView.visibility = View.GONE
                binding.borderBlackView.visibility = View.GONE
                binding.appBar.visibility = View.GONE
            } else {
                binding.appBar.visibility = View.VISIBLE
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.borderBlackView.visibility = View.VISIBLE

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}