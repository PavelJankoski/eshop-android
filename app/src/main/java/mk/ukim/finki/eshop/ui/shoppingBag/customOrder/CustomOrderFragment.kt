package mk.ukim.finki.eshop.ui.shoppingBag.customOrder

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.OrderProductsAdapter
import mk.ukim.finki.eshop.adapters.ProductsListAdapter
import mk.ukim.finki.eshop.databinding.FragmentCustomOrderBinding
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagViewModel
import mk.ukim.finki.eshop.util.Constants
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
                if (value.message.equals("")) {
                    setupNoInternetConnectionView()
                }
            }
        })
    }

    private fun setupUserHasNoActiveCartUI() {
        binding.imageViewEmptyBag.visibility = View.GONE
        binding.textViewEmptyBag.visibility = View.GONE
        binding.wifiOffSb.visibility = View.GONE
        binding.textViewWifiOffMessage.visibility = View.GONE
        binding.shoppingBagShimmerRecyclerView.visibility = View.GONE
        binding.textViewTotalCost.visibility = View.GONE
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
        binding.textViewTotalCost.visibility = View.GONE
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
        binding.textViewTotalCost.visibility = View.GONE
        binding.textViewTotalCostLabel.visibility = View.GONE
        binding.createShoppingCartBtn.visibility = View.GONE
        binding.imageViewNoActiveShoppingBag.visibility = View.GONE
        binding.textViewNoActiveShoppingBag.visibility = View.GONE
        binding.checkoutBtn.visibility = View.GONE
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
        binding.textViewTotalCost.visibility = View.VISIBLE
        binding.textViewTotalCostLabel.visibility = View.VISIBLE
        binding.checkoutBtn.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        binding.shoppingBagShimmerRecyclerView.adapter = mAdapterList
        binding.shoppingBagShimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        Utils.showShimmerEffect(binding.customOrderShimmerFrameLayout, binding.shoppingBagShimmerRecyclerView)
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