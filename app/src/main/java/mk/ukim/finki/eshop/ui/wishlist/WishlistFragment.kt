package mk.ukim.finki.eshop.ui.wishlist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.WishlistAdapter
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import javax.inject.Inject

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val wishlistViewModel by viewModels<WishlistViewModel>()
    private val mAdapter by lazy { WishlistAdapter(wishlistViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)

        if (!loginManager.loggedIn.value) {
            setupUserNotAuthenticatedInterface()
            showLoginPrompt()
        } else {
            setupRecyclerView()
            observeWishlistProducts()
            setHasOptionsMenu(true)
            binding.startShoppingBtn.setOnClickListener {
                requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.categoriesFragment
            }
        }
        return binding.root
    }

    private fun setupUserNotAuthenticatedInterface() {
        hideShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
        binding.wishlistRecyclerView.visibility = View.GONE
        binding.wishlistIv.setImageResource(R.drawable.ic_forbidden)
        binding.wishlistIv.visibility = View.VISIBLE
        binding.startShoppingBtn.visibility = View.GONE
        binding.wishlistEmptyTextView.text = "Please login to access wishlist"
        binding.wishlistEmptyTextView.visibility = View.VISIBLE
    }

    private fun showLoginPrompt() {
        findNavController().navigate(R.id.action_wishlistFragment_to_loginPrompt)
    }

    private fun setupRecyclerView() {
        hideShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
        binding.wishlistRecyclerView.adapter = mAdapter
        binding.wishlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeWishlistProducts()  {
        wishlistViewModel.getCartItems()
        wishlistViewModel.wishlistProductsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
                    if (!response.data.isNullOrEmpty()) {
                        setupWishlistNotEmpty()
                        mAdapter.setData(response.data)
                    } else {
                        setupWishlistEmpty()
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
                }
            }
        })
    }

    private fun setupWishlistEmpty() {
        binding.wishlistIv.setImageResource(R.drawable.ic_wishlist_empty)
        binding.wishlistRecyclerView.visibility = View.GONE
        binding.wishlistIv.visibility = View.VISIBLE
        binding.startShoppingBtn.visibility = View.VISIBLE
        binding.wishlistEmptyTextView.text = "Your wishlist is empty"
        binding.wishlistEmptyTextView.visibility = View.VISIBLE
    }

    private fun setupWishlistNotEmpty() {
        binding.wishlistIv.visibility = View.GONE
        binding.startShoppingBtn.visibility = View.GONE
        binding.wishlistEmptyTextView.visibility = View.GONE
        binding.wishlistRecyclerView.visibility = View.VISIBLE
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
                findNavController().navigate(R.id.action_wishlistFragment_to_shoppingBagFragment)
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