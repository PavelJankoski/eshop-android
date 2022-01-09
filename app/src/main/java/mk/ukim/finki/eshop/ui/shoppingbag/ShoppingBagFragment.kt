package mk.ukim.finki.eshop.ui.shoppingbag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.AddressBookAdapter
import mk.ukim.finki.eshop.adapters.ShoppingBagAdapter
import mk.ukim.finki.eshop.databinding.FragmentDetailsBinding
import mk.ukim.finki.eshop.databinding.FragmentShoppingBagBinding
import mk.ukim.finki.eshop.ui.addressbook.AddressBookViewModel
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
        shoppingBagViewModel.getOrderItemsForUser()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.shoppingBagRecyclerView.adapter = mAdapter
        binding.shoppingBagRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeOrderItemsResponse() {
        shoppingBagViewModel.orderItemsResponse.observe(viewLifecycleOwner, {response ->
            when(response) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(
                        binding.shoppingBagShimmerFrameLayout,
                        binding.shoppingBagRecyclerView
                    )
                    if(!response.data.isNullOrEmpty()) {
                        shoppingBagNotEmpty()
                        mAdapter.setData(response.data)
                    }
                    else {
                        shoppingBagEmpty()
                    }
                }
                is NetworkResult.Error -> {
                    Utils.hideShimmerEffect(
                        binding.shoppingBagShimmerFrameLayout,
                        binding.shoppingBagRecyclerView
                    )
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