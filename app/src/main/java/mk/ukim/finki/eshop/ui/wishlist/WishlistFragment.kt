package mk.ukim.finki.eshop.ui.wishlist

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.WishlistListAdapter
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import mk.ukim.finki.eshop.util.Utils.Companion.showShimmerEffect

@AndroidEntryPoint
class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val wishlistViewModel by viewModels<WishlistViewModel>()
    private val mAdapter by lazy { WishlistListAdapter(wishlistViewModel) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeWishlistProducts()
        setHasOptionsMenu(true)
        binding.startShoppingBtn.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.categoriesFragment
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        hideShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
        binding.wishlistRecyclerView.adapter = mAdapter
        binding.wishlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeWishlistProducts()  {
        lifecycleScope.launch {
            wishlistViewModel.readWishlistProducts.observe(viewLifecycleOwner, {table ->
                if(table.isNullOrEmpty()) {
                    setupWishlistEmpty()
                }
                else {
                    setupWishlistNotEmpty()
                }
                mAdapter.setData(table.sortedByDescending { it.addedOn })
            })
        }
    }

    private fun setupWishlistEmpty() {
        binding.wishlistRecyclerView.visibility = View.GONE
        binding.wishlistIv.visibility = View.VISIBLE
        binding.startShoppingBtn.visibility = View.VISIBLE
        binding.wishlistEmptyTextView.visibility = View.VISIBLE
    }

    private fun setupWishlistNotEmpty() {
        binding.wishlistIv.visibility = View.GONE
        binding.startShoppingBtn.visibility = View.GONE
        binding.wishlistEmptyTextView.visibility = View.GONE
        binding.wishlistRecyclerView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.categories_toolbar_menu, menu)
        val menuItem = menu.findItem(R.id.shoppingCart_menuItem)
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoppingCart_menuItem -> {
                Utils.showToast(requireContext(), "Bag clicked!", Toast.LENGTH_SHORT)
                true
            }
            R.id.search_menuItem -> {
                Utils.showToast(requireContext(), "Search clicked!", Toast.LENGTH_SHORT)
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