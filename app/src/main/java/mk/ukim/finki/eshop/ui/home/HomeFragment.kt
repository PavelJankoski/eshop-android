package mk.ukim.finki.eshop.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.HomePagerAdapter
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding
import mk.ukim.finki.eshop.databinding.FragmentHomeBinding
import mk.ukim.finki.eshop.ui.home.men.MenFragment
import mk.ukim.finki.eshop.ui.home.women.WomenFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewPager()
        return binding.root
    }

    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(MenFragment(), WomenFragment())
        val tabLayoutTitles = arrayListOf<String>("Man", "Woman")
        val pagerAdapter = HomePagerAdapter(fragments, this)
        binding.viewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}