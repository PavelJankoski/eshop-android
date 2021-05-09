package mk.ukim.finki.eshop.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.CategoriesPagerAdapter
import mk.ukim.finki.eshop.databinding.FragmentHomeBinding
import mk.ukim.finki.eshop.ui.home.man.HomeManFragment
import mk.ukim.finki.eshop.ui.home.woman.HomeWomanFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(HomeManFragment(), HomeWomanFragment())
        val tabLayoutTitles = arrayListOf<String>("Man", "Woman")
        val pagerAdapter = CategoriesPagerAdapter(fragments, this)
        binding.viewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.categories_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoppingCart_menuItem -> {
                Toast.makeText(requireContext(), "Cart clicked", Toast.LENGTH_LONG).show()
                true
            }
            R.id.search_menuItem -> {
                Toast.makeText(requireContext(), "Search clicked", Toast.LENGTH_LONG).show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}