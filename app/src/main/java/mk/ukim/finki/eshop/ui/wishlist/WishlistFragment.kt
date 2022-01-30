package mk.ukim.finki.eshop.ui.wishlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.WishlistAdapter
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import javax.inject.Inject

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    @Inject
    lateinit var loginManager: LoginManager

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val wishlistViewModel by viewModels<WishlistViewModel>()
    private val mAdapter by lazy { WishlistAdapter(wishlistViewModel) }
    private lateinit var menuItem: MenuItem

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
            observeRemoveProductFromWishlist()
            observeSwipeRemoveProduct()
            observeMoveProductToBag()
            setHasOptionsMenu(true)
            wishlistViewModel.getWishlistProductsForUser()

        }
        return binding.root
    }

    private fun observeSwipeRemoveProduct() {
        val swipeHandler = object : WishlistSwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.wishlistRecyclerView.adapter as WishlistAdapter
                val position = viewHolder.absoluteAdapterPosition
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete wishlist product")
                    .setMessage("Are you sure you want to remove this product from wishlist?")
                    .setPositiveButton("Yes") { _, _ ->
                        wishlistViewModel.removeProductFromWishlistForUser(
                            adapter.getProduct(
                                position
                            ).id
                        )
                        adapter.notifyItemChanged(position)
                    }
                    .setNegativeButton("Cancel") { _, _ -> adapter.notifyItemChanged(position) }
                    .show()

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.wishlistRecyclerView)

    }

    private fun observeMoveProductToBag() {
        wishlistViewModel.addProductToShoppingBagResponse.value = NetworkResult.Loading()
        wishlistViewModel.addProductToShoppingBagResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    wishlistViewModel.removeProductFromWishlistForUser(it.data!!)
                    val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
                    Utils.setupCartItemsBadge(tv, MyApplication.itemsInBag)
                    Utils.showSnackbar(
                        binding.root,
                        "Moved product to shopping bag!",
                        Snackbar.LENGTH_SHORT
                    )
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(
                        binding.root,
                        "Error moving product to shopping bag!",
                        Snackbar.LENGTH_SHORT
                    )
                }
                else -> {}
            }
        }
    }

    private fun setupUserNotAuthenticatedInterface() {
        hideShimmerEffect(binding.wishlistShimmerFrameLayout, binding.wishlistRecyclerView)
        binding.wishlistRecyclerView.visibility = View.GONE
        binding.wishlistIv.setImageResource(R.drawable.ic_forbidden)
        binding.wishlistIv.visibility = View.VISIBLE
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

    private fun observeWishlistProducts() {
        wishlistViewModel.wishlistProductsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect(
                        binding.wishlistShimmerFrameLayout,
                        binding.wishlistRecyclerView
                    )
                    if (!response.data.isNullOrEmpty()) {
                        setupWishlistNotEmpty()
                        mAdapter.setData(response.data)
                    } else {
                        setupWishlistEmpty()
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect(
                        binding.wishlistShimmerFrameLayout,
                        binding.wishlistRecyclerView
                    )
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(
                        binding.wishlistShimmerFrameLayout,
                        binding.wishlistRecyclerView
                    )
                }
            }
        }
    }

    private fun observeRemoveProductFromWishlist() {
        wishlistViewModel.removeProductFromWishlistResponse.value = NetworkResult.Loading()
        wishlistViewModel.removeProductFromWishlistResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    wishlistViewModel.removeProductFromWishlistAfterResponse(it.data!!)
                    Utils.showSnackbar(
                        binding.root,
                        "Removed product from wishlist!",
                        Snackbar.LENGTH_SHORT
                    )
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(
                        binding.root,
                        "Error removing product from wishlist!",
                        Snackbar.LENGTH_SHORT
                    )
                }
                else -> {}
            }
        }
    }

    private fun setupWishlistEmpty() {
        binding.wishlistIv.setImageResource(R.drawable.ic_wishlist_empty)
        binding.wishlistRecyclerView.visibility = View.GONE
        binding.wishlistIv.visibility = View.VISIBLE
        binding.wishlistEmptyTextView.text = "Your wishlist is empty"
        binding.wishlistEmptyTextView.visibility = View.VISIBLE
    }

    private fun setupWishlistNotEmpty() {
        binding.wishlistIv.visibility = View.GONE
        binding.wishlistEmptyTextView.visibility = View.GONE
        binding.wishlistRecyclerView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopping_cart_toolbar_menu, menu)
        menuItem = menu.findItem(R.id.shoppingCart_menuItem)
        val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
        Utils.setupCartItemsBadge(tv, MyApplication.itemsInBag)
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
                    findNavController().navigate(WishlistFragmentDirections.actionWishlistFragmentToShoppingBagFragment())
                }
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