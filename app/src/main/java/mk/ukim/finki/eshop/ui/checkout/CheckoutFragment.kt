package mk.ukim.finki.eshop.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentCheckoutBinding
import mk.ukim.finki.eshop.util.Constants.Companion.STRIPE_PUBLISHABLE_KEY
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class CheckoutFragment : Fragment() {
    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val checkoutViewModel by viewModels<CheckoutViewModel>()
    private lateinit var paymentSheet: PaymentSheet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        setupStripe()
        binding.placeOrderBtn.setOnClickListener {
            presentPaymentSheet()
        }
        binding.changeAddressBtn.setOnClickListener {
            findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToAddressBookFragment())
        }
        checkoutViewModel.getOrderDetailsForUser()
        observeOrderDetails()
        observePaymentSheetParams()
        observePlaceOrder()
        return binding.root
    }

    private fun observePlaceOrder() {
        checkoutViewModel.placeOrderResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Utils.showSnackbar(
                        binding.root,
                        "Items purchased successfully!",
                        Snackbar.LENGTH_SHORT
                    )
                    MyApplication.itemsInBag = 0
                    findNavController().popBackStack(R.id.categoriesFragment, false)
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(binding.root, it.message!!, Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        }
    }

    private fun setupStripe() {
        PaymentConfiguration.init(requireContext(), STRIPE_PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this) { result ->
            onPaymentSheetResult(result)
        }
    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            checkoutViewModel.paymentSheetParamsResponse.value!!.data!!.paymentIntent,
            PaymentSheet.Configuration(
                merchantDisplayName = "Clothing shop, Inc.",
                customer = PaymentSheet.CustomerConfiguration(
                    id = checkoutViewModel.paymentSheetParamsResponse.value!!.data!!.customer,
                    ephemeralKeySecret = checkoutViewModel.paymentSheetParamsResponse.value!!.data!!.ephemeralKey,
                )
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.i("onPaymentSheetResult", "Payment cancelled")
            }
            is PaymentSheetResult.Failed -> {
                Utils.showSnackbar(
                    binding.root,
                    "Due to technical problems the payment failed, try again later...",
                    Toast.LENGTH_SHORT
                )
                Log.e("onPaymentSheetResult", "Got error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                checkoutViewModel.placeOrder()
            }
        }
    }

    private fun observePaymentSheetParams() {
        checkoutViewModel.paymentSheetParamsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (checkoutViewModel.orderDetailsResponse.value!!.data?.address != null) {
                        enableCheckoutButton()
                    }
                }
                is NetworkResult.Error -> {
                    disableCheckoutButton()
                    Utils.showToast(requireContext(), response.message!!, Toast.LENGTH_LONG)
                }
                is NetworkResult.Loading -> {
                    disableCheckoutButton()
                    showShimmerEffect()
                }
            }
        }
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

    private fun enableCheckoutButton() {
        binding.placeOrderBtn.isEnabled = true
        binding.placeOrderBtn.setBackgroundColor(binding.placeOrderBtn.context.resources.getColor(R.color.green))
    }

    private fun disableCheckoutButton() {
        binding.placeOrderBtn.isEnabled = false
        binding.placeOrderBtn.setBackgroundColor(binding.placeOrderBtn.context.resources.getColor(R.color.lightMediumGray))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}