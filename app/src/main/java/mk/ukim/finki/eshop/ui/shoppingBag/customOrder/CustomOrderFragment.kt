package mk.ukim.finki.eshop.ui.shoppingBag.customOrder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.OrderProductsAdapter
import mk.ukim.finki.eshop.databinding.FragmentCustomOrderBinding
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagViewModel
import mk.ukim.finki.eshop.ui.shoppingBag.customOrder.swipeToDelete.SwipeToDeleteCallback
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class CustomOrderFragment : Fragment() {

    private var _binding: FragmentCustomOrderBinding? = null
    private val binding get() = _binding!!
    private val shoppingBagViewModel: ShoppingBagViewModel by activityViewModels()
    private val mAdapterList by lazy { OrderProductsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomOrderBinding.inflate(inflater, container, false)

        setupUserHasActiveCartObserver()
        observeProductsResponse()
        observeSwipeRemoveProduct()
        observeTotalPrice()
        setupRecyclerView()

        return binding.root
    }

    private fun setupUserHasActiveCartObserver() {
        shoppingBagViewModel.activeShoppingCartExistsResponse.observe(viewLifecycleOwner, { value ->
            if (value is NetworkResult.Success) {
                if (value.data == true) {
                    shoppingBagViewModel.getCartItems()
                } else {
                    setupUserHasNoActiveCartUI()
                }
            } else if (value is NetworkResult.Error) {
//                if (value.message.equals("")) {
//                    setupNoInternetConnectionView()
//                }
            }
        })
    }

    private fun setupUserHasNoActiveCartUI() {
        binding.imageViewEmptyBag.visibility = View.GONE
        binding.textViewEmptyBag.visibility = View.GONE
        binding.wifiOffSb.visibility = View.GONE
        binding.textViewWifiOffMessage.visibility = View.GONE
        binding.shoppingBagShimmerRecyclerView.visibility = View.GONE
        binding.totalCostTextView.visibility = View.GONE
        binding.textViewTotalCostLabel.visibility = View.GONE
        binding.checkoutBtn.visibility = View.GONE

        binding.imageViewNoActiveShoppingBag.visibility = View.VISIBLE
        binding.textViewNoActiveShoppingBag.visibility = View.VISIBLE
        binding.createShoppingCartBtn.visibility = View.VISIBLE
    }


    private fun setupUserHasEmptyBag() {
        binding.wifiOffSb.visibility = View.GONE
        binding.textViewWifiOffMessage.visibility = View.GONE
        binding.shoppingBagShimmerRecyclerView.visibility = View.GONE
        binding.totalCostTextView.visibility = View.GONE
        binding.textViewTotalCostLabel.visibility = View.GONE
        binding.createShoppingCartBtn.visibility = View.GONE
        binding.imageViewNoActiveShoppingBag.visibility = View.GONE
        binding.textViewNoActiveShoppingBag.visibility = View.GONE
        binding.checkoutBtn.visibility = View.GONE

        binding.imageViewEmptyBag.visibility = View.VISIBLE
        binding.textViewEmptyBag.visibility = View.VISIBLE
    }

    private fun setupNoInternetConnectionView() {
        binding.shoppingBagShimmerRecyclerView.visibility = View.GONE
        binding.checkoutConstraintLayout.visibility = View.GONE
        binding.createShoppingCartBtn.visibility = View.GONE
        binding.imageViewNoActiveShoppingBag.visibility = View.GONE
        binding.textViewNoActiveShoppingBag.visibility = View.GONE
        binding.imageViewEmptyBag.visibility = View.GONE
        binding.textViewEmptyBag.visibility = View.GONE

        binding.wifiOffSb.visibility = View.VISIBLE
        binding.textViewWifiOffMessage.visibility = View.VISIBLE
    }

    private fun setupUserHasNoEmptyBag() {
        binding.imageViewEmptyBag.visibility = View.GONE
        binding.textViewEmptyBag.visibility = View.GONE
        binding.wifiOffSb.visibility = View.GONE
        binding.textViewWifiOffMessage.visibility = View.GONE
        binding.imageViewNoActiveShoppingBag.visibility = View.GONE
        binding.textViewNoActiveShoppingBag.visibility = View.GONE
        binding.createShoppingCartBtn.visibility = View.GONE

        binding.shoppingBagShimmerRecyclerView.visibility = View.VISIBLE
        binding.checkoutConstraintLayout.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.shoppingBagShimmerRecyclerView.adapter = mAdapterList
        binding.shoppingBagShimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.shoppingBagShimmerRecyclerView.adapter as OrderProductsAdapter
                val position = viewHolder.absoluteAdapterPosition
                val cartItem = adapter.getCartItem(position)
                val price = cartItem.product.price * cartItem.quantity

                shoppingBagViewModel.removeProductFromShoppingCart(adapter.getProduct(position), price.toInt())
                adapter.removeProduct(position)

                Snackbar.make(binding.shoppingBagShimmerRecyclerView, "Undo remove of products", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        adapter.insertProduct(position, cartItem)
                        shoppingBagViewModel.addProductToShoppingCart(adapter.getProduct(position), price.toInt())
                    }.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white)).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.shoppingBagShimmerRecyclerView)

        Utils.showShimmerEffect(binding.customOrderShimmerFrameLayout, binding.shoppingBagShimmerRecyclerView)
    }

    private fun observeTotalPrice() {
        shoppingBagViewModel.shoppingBagManager.totalPrice.observe(viewLifecycleOwner, {
            if(it == 0) {
                binding.checkoutConstraintLayout.visibility = View.GONE
            }
            else {
                binding.checkoutConstraintLayout.visibility = View.VISIBLE
                binding.totalCostTextView.text = it.toString().plus(" MKD")
            }
        })
    }

    private fun observeSwipeRemoveProduct() {
        shoppingBagViewModel.shoppingBagManager.removeProductFromBagResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Success) {
                shoppingBagViewModel.getCartItems()
            }
        })
    }

    private fun observeProductsResponse() {
        shoppingBagViewModel.cartItemsResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(binding.customOrderShimmerFrameLayout, binding.shoppingBagShimmerRecyclerView)
                    if (!response.data.isNullOrEmpty()) {
                        setupUserHasNoEmptyBag()
                        mAdapterList.setData(response.data)
                    } else {
                        setupUserHasEmptyBag()
                    }
                }
                is NetworkResult.Error -> {
                    Utils.hideShimmerEffect(binding.customOrderShimmerFrameLayout, binding.shoppingBagShimmerRecyclerView)
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(binding.customOrderShimmerFrameLayout, binding.shoppingBagShimmerRecyclerView)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shoppingBagViewModel.checkIfUserHasActiveShoppingCart()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}