package mk.ukim.finki.eshop.ui.orderhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.OrderHistoryAdapter
import mk.ukim.finki.eshop.databinding.FragmentOrderHistoryBinding
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private val orderHistoryViewModel by viewModels<OrderHistoryViewModel>()
    private val mAdapter by lazy { OrderHistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeOrderHistory()
        orderHistoryViewModel.getOrderHistoryForUser()
        return binding.root
    }

    private fun observeOrderHistory() {
        orderHistoryViewModel.orderHistoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(
                        binding.orderHistoryShimmerFrameLayout,
                        binding.orderHistoryRecyclerView
                    )
                    if (it.data!!.orderHistoryItems.isNullOrEmpty()) {
                        orderHistoryEmpty()
                    } else {
                        orderHistoryNotEmpty()
                        binding.amountTextView.text = it.data.totalOrders.toString().plus(" orders")
                        mAdapter.setData(it.data.orderHistoryItems)
                    }
                }
                is NetworkResult.Error -> {
                    orderHistoryEmpty()
                    Utils.hideShimmerEffect(
                        binding.orderHistoryShimmerFrameLayout,
                        binding.orderHistoryRecyclerView
                    )
                }
                else -> {
                    Utils.showShimmerEffect(
                        binding.orderHistoryShimmerFrameLayout,
                        binding.orderHistoryRecyclerView
                    )
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.orderHistoryRecyclerView.adapter = mAdapter
        binding.orderHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun orderHistoryEmpty() {
        binding.orderHistoryRecyclerView.visibility = View.GONE
        binding.shoppingBagErrorOrderHistoryLottie.visibility = View.VISIBLE
        binding.emptyTextView.visibility = View.VISIBLE
    }

    private fun orderHistoryNotEmpty() {
        binding.orderHistoryRecyclerView.visibility = View.VISIBLE
        binding.shoppingBagErrorOrderHistoryLottie.visibility = View.GONE
        binding.emptyTextView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}