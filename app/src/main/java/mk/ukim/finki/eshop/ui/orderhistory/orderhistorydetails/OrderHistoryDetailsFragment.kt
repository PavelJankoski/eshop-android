package mk.ukim.finki.eshop.ui.orderhistory.orderhistorydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.OrderHistoryDetailsAdapter
import mk.ukim.finki.eshop.databinding.FragmentOrderHistoryDetailsBinding
import mk.ukim.finki.eshop.ui.orderhistory.OrderHistoryViewModel
import mk.ukim.finki.eshop.util.NetworkResult

@AndroidEntryPoint
class OrderHistoryDetailsFragment : Fragment() {
    private var _binding: FragmentOrderHistoryDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<OrderHistoryDetailsFragmentArgs>()
    private val orderHistoryViewModel by viewModels<OrderHistoryViewModel>()
    private val mAdapter by lazy { OrderHistoryDetailsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        setupRecyclerView()
        observeOrderHistoryDetails()
        orderHistoryViewModel.getOrderHistoryDetails(args.orderId)
        return binding.root
    }

    private fun observeOrderHistoryDetails() {
        orderHistoryViewModel.orderHistoryDetailsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    binding.orderHistory = it.data
                    mAdapter.setData(it.data!!.orderProducts)
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                }
                else -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.orderHistoryRecyclerView.adapter = mAdapter
        binding.orderHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showShimmerEffect() {
        binding.orderHistoryShimmerFrameLayout.startShimmer()
        binding.orderHistoryShimmerFrameLayout.visibility = View.VISIBLE
        binding.detailsLinearLayout.visibility = View.GONE
        binding.deliveryLinearLayout.visibility = View.GONE
        binding.orderItemsConstraintLayout.visibility = View.GONE
        binding.totalConstraintLayout.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        if (binding.orderHistoryShimmerFrameLayout.isShimmerVisible) {
            binding.orderHistoryShimmerFrameLayout.visibility = View.GONE
            binding.orderHistoryShimmerFrameLayout.stopShimmer()
        }
        binding.detailsLinearLayout.visibility = View.VISIBLE
        binding.deliveryLinearLayout.visibility = View.VISIBLE
        binding.orderItemsConstraintLayout.visibility = View.VISIBLE
        binding.totalConstraintLayout.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}