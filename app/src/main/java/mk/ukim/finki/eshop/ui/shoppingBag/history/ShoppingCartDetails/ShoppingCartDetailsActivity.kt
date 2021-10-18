package mk.ukim.finki.eshop.ui.shoppingBag.history.ShoppingCartDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.OrderProductsAdapter
import mk.ukim.finki.eshop.databinding.ActivityShoppingCartDetailsBinding

class ShoppingCartDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingCartDetailsBinding
    private val args by navArgs<ShoppingCartDetailsActivityArgs>()

    private val mAdapterList by lazy { OrderProductsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartDetailsBinding.inflate(layoutInflater)
        binding.shoppingCart = args.shoppingCart
        binding.lifecycleOwner = this
        setContentView(binding.root)
        setupRecyclerView()

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        binding.productListDetailsRecyclerView.adapter = mAdapterList
        binding.productListDetailsRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mAdapterList.setData(args.shoppingCart.cartItems)
    }
}