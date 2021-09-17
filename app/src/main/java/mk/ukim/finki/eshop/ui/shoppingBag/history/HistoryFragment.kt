package mk.ukim.finki.eshop.ui.shoppingBag.history

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
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.HistoryAdapter
import mk.ukim.finki.eshop.adapters.OrderProductsAdapter
import mk.ukim.finki.eshop.databinding.FragmentCustomOrderBinding
import mk.ukim.finki.eshop.databinding.FragmentHistoryBinding
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagViewModel
import mk.ukim.finki.eshop.ui.shoppingBag.customOrder.swipeToDelete.SwipeToDeleteCallback
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val shoppingBagViewModel: ShoppingBagViewModel by activityViewModels()
    private val mAdapterList by lazy { HistoryAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        observeShoppingCartsResponse()
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.historyShimmerRecyclerView.adapter = mAdapterList
        binding.historyShimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        Utils.showShimmerEffect(binding.historyShimmerFrameLayout, binding.historyShimmerRecyclerView)
    }

    private fun observeShoppingCartsResponse() {
        shoppingBagViewModel.historyShoppingCartsResponse.observe(viewLifecycleOwner, {response ->
            when (response) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(binding.historyShimmerFrameLayout, binding.historyShimmerRecyclerView)
                    if (!response.data.isNullOrEmpty()) {
                        mAdapterList.setData(response.data)
                    }
                }
                is NetworkResult.Error -> {
                    Utils.hideShimmerEffect(binding.historyShimmerFrameLayout, binding.historyShimmerRecyclerView)
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(binding.historyShimmerFrameLayout, binding.historyShimmerRecyclerView)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
        shoppingBagViewModel.fetchHistoryShoppingCarts()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}