package mk.ukim.finki.eshop.ui.shoppingBag.customOrder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.OrderProductsAdapter
import mk.ukim.finki.eshop.databinding.FragmentCustomOrderBinding
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagViewModel
import mk.ukim.finki.eshop.ui.shoppingBag.customOrder.swipeToDelete.SwipeToDeleteCallback
import mk.ukim.finki.eshop.util.Constants.Companion.STRIPE_PUBLISHABLE_KEY
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class CustomOrderFragment : Fragment() {

    private lateinit var paymentSheet: PaymentSheet

    private lateinit var customerId: String
    private lateinit var ephemeralKeySecret: String
    private lateinit var paymentIntentClientSecret: String

    private var _binding: FragmentCustomOrderBinding? = null
    private val binding get() = _binding!!
    private val shoppingBagViewModel: ShoppingBagViewModel by activityViewModels()
    private val mAdapterList by lazy { OrderProductsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PaymentConfiguration.init(requireContext(), STRIPE_PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this) { result ->
            onPaymentSheetResult(result)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomOrderBinding.inflate(inflater, container, false)
        binding.checkoutBtn.setOnClickListener {
            presentPaymentSheet()
        }
        binding.checkoutBtn.isEnabled = false
        fetchInitData()

        observeProductsResponse()
        observeSwipeRemoveProduct()
        observeTotalPrice()
        setupRecyclerView()
        observePaymentSheetParamrs()
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
                        shoppingBagViewModel.addProductToShoppingCart(cartItem.product.id, price.toInt())
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
        shoppingBagViewModel.shoppingBagManager.addOrRemoveProductResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Success) {
                fetchInitData()
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

    private fun fetchInitData() {
        binding.checkoutBtn.isEnabled = false
        shoppingBagViewModel.fetchPaymentSheetParams()
    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Example, Inc.",
                customer = PaymentSheet.CustomerConfiguration(
                    id = customerId,
                    ephemeralKeySecret = ephemeralKeySecret
                )
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Utils.showToast(requireContext(), "Payment cancelled", Toast.LENGTH_LONG)
            }
            is PaymentSheetResult.Failed -> {
                Utils.showToast(
                    requireContext(),
                    "Due to technical problems the payment failed, try again later...",
                    Toast.LENGTH_LONG
                )
                Log.e("App", "Got error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                Utils.showToast(requireContext(), "Payment Complete", Toast.LENGTH_LONG)
            }
        }
    }

    private fun observePaymentSheetParamrs() {
        shoppingBagViewModel.paymentParamsResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Success) {
                Utils.showSnackbar(binding.shoppingBagShimmerRecyclerView, "You're info is validated now. You can execute payments.", Snackbar.LENGTH_LONG);
                customerId = response.data!!.get("customer")!!
                ephemeralKeySecret = response.data.get("ephemeralKey")!!
                paymentIntentClientSecret = response.data.get("paymentIntent")!!
                binding.checkoutBtn.isEnabled=true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shoppingBagViewModel.checkIfUserHasActiveShoppingCart()
        setupUserHasActiveCartObserver()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}