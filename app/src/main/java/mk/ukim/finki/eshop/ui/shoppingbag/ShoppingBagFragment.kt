package mk.ukim.finki.eshop.ui.shoppingbag

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.ShoppingBagAdapter
import mk.ukim.finki.eshop.adapters.WishlistAdapter
import mk.ukim.finki.eshop.databinding.FragmentShoppingBagBinding
import mk.ukim.finki.eshop.ui.wishlist.WishlistSwipeToDeleteCallback
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class ShoppingBagFragment : Fragment() {
    private var _binding: FragmentShoppingBagBinding? = null
    private val binding get() = _binding!!
    private val shoppingBagViewModel by viewModels<ShoppingBagViewModel>()
    private val mAdapter by lazy { ShoppingBagAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBagBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeOrderItemsResponse()
        observeRemoveProductFromBag()
        observeSwipeRemoveProduct()
        shoppingBagViewModel.getOrderItemsForUser()
        return binding.root
    }

    private fun observeRemoveProductFromBag() {
        shoppingBagViewModel.removeProductFromShoppingBagResponse.value = NetworkResult.Loading()
        shoppingBagViewModel.removeProductFromShoppingBagResponse.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkResult.Success -> {
                    shoppingBagViewModel.getOrderItemsForUser()
                    Utils.showSnackbar(
                        binding.root,
                        "Removed product from shopping bag!",
                        Snackbar.LENGTH_SHORT
                    )
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(
                        binding.root,
                        "Error removing product from shoppingg bag!",
                        Snackbar.LENGTH_SHORT
                    )
                }
                else -> {}
            }
        })

    }

    private fun setupRecyclerView() {
        binding.shoppingBagRecyclerView.adapter = mAdapter
        binding.shoppingBagRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeOrderItemsResponse() {
        shoppingBagViewModel.orderItemsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(
                        binding.shoppingBagShimmerFrameLayout,
                        binding.shoppingBagRecyclerView
                    )
                    if (!response.data.isNullOrEmpty()) {
                        shoppingBagNotEmpty()
                        mAdapter.setData(response.data)
                    } else {
                        shoppingBagEmpty()
                    }
                }
                is NetworkResult.Error -> {
                    Utils.hideShimmerEffect(
                        binding.shoppingBagShimmerFrameLayout,
                        binding.shoppingBagRecyclerView
                    )
                    shoppingBagEmpty()
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(
                        binding.shoppingBagShimmerFrameLayout,
                        binding.shoppingBagRecyclerView
                    )
                }
            }
        })
    }

    private fun observeSwipeRemoveProduct() {
        val swipeHandler = object : ShoppingBagSwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.shoppingBagRecyclerView.adapter as ShoppingBagAdapter
                val position = viewHolder.absoluteAdapterPosition
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete product from shopping bag")
                    .setMessage("Are you sure you want to remove this product from your shopping bag?")
                    .setPositiveButton("Yes") { _, _ ->
                        val orderItem = adapter.getProduct(
                            position
                        )
                        shoppingBagViewModel.removeProductFromShoppingBag(
                            orderItem.productId, orderItem.sizes.find { s -> s.name == orderItem.selectedSize}!!.id
                        )
                    }
                    .setNegativeButton("Cancel") { _, _ -> adapter.notifyItemChanged(position) }
                    .show()

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.shoppingBagRecyclerView)
    }

    private fun shoppingBagEmpty() {
        binding.shoppingBagRecyclerView.visibility = View.GONE
        binding.checkoutConstraintLayout.visibility = View.GONE
        binding.shoppingBagErrorShoppingBagLottie.visibility = View.VISIBLE
        binding.emptyTextView.visibility = View.VISIBLE
    }

    private fun shoppingBagNotEmpty() {
        binding.shoppingBagRecyclerView.visibility = View.VISIBLE
        binding.checkoutConstraintLayout.visibility = View.VISIBLE
        binding.shoppingBagErrorShoppingBagLottie.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}