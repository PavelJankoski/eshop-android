package mk.ukim.finki.eshop.ui.categories

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.PagerAdapter
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.categories.man.CategoriesManFragment
import mk.ukim.finki.eshop.ui.categories.woman.CategoriesWomanFragment
import mk.ukim.finki.eshop.util.Utils
import javax.inject.Inject


@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = categoriesViewModel
        setHasOptionsMenu(true)
        setupViewPager()
        categoriesViewModel.getCategories()
        return binding.root
    }



    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(CategoriesManFragment(), CategoriesWomanFragment())
        val tabLayoutTitles = arrayListOf<String>("Man", "Woman")
        val pagerAdapter = PagerAdapter(fragments, this)
        binding.viewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopping_cart_toolbar_menu, menu)
        val menuItem = menu.findItem(R.id.shoppingCart_menuItem)
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoppingCart_menuItem -> {
                if (!loginManager.loggedIn.value) {
                    showLoginPrompt()
                } else {
                    navigateToShoppingBag()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToShoppingBag() {
        findNavController().navigate(R.id.action_categoriesFragment_to_shoppingBagFragment)
    }

    private fun showLoginPrompt() {
        findNavController().navigate(R.id.action_categoriesFragment_to_loginPrompt)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}