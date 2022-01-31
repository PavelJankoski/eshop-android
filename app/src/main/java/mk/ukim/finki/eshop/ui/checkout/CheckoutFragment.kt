package mk.ukim.finki.eshop.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.databinding.FragmentCheckoutBinding
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val checkoutViewModel by viewModels<CheckoutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        checkoutViewModel.getOrderDetailsForUser()
        observeOrderDetails()
        return binding.root
    }

    private fun observeOrderDetails() {
        checkoutViewModel.orderDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.orderDetails = checkoutViewModel.orderDetailsResponse.value!!.data
                    hideShimmerEffect()
                }
                is NetworkResult.Error -> {
                    Utils.showToast(requireContext(), response.message!!, Toast.LENGTH_LONG)
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.checkoutShimmerFrameLayout.startShimmer()
        binding.checkoutShimmerFrameLayout.visibility = View.VISIBLE
        binding.cardViewsConstraintLayout.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        if (binding.checkoutShimmerFrameLayout.isShimmerVisible) {
            binding.checkoutShimmerFrameLayout.visibility = View.GONE
            binding.checkoutShimmerFrameLayout.stopShimmer()
        }
        binding.cardViewsConstraintLayout.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}