package mk.ukim.finki.eshop.ui.shoppingBag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import mk.ukim.finki.eshop.adapters.PagerAdapter
import mk.ukim.finki.eshop.databinding.FragmentShoppingBagBinding
import mk.ukim.finki.eshop.ui.shoppingBag.customOrder.CustomOrderFragment
import mk.ukim.finki.eshop.ui.shoppingBag.history.HistoryFragment

class ShoppingBagFragment : Fragment() {

    private var _binding: FragmentShoppingBagBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBagBinding.inflate(inflater, container, false)
        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(CustomOrderFragment(), HistoryFragment())
        val tabLayoutTitles = arrayListOf<String>("Custom Order", "History")
        val pagerAdapter = PagerAdapter(fragments, this)
        binding.shoppingCartViewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.shoppingBagTabLayout, binding.shoppingCartViewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()

        binding.shoppingCartViewPager.measure(
            View.MeasureSpec.makeMeasureSpec(binding.root.measuredWidth, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(binding.root.measuredHeight, View.MeasureSpec.EXACTLY)
        )

        binding.shoppingCartViewPager.isUserInputEnabled = false;
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}