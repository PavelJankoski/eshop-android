package mk.ukim.finki.eshop.ui.shoppingBag.history.ShoppingCartDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.ActivityDetailsBinding
import mk.ukim.finki.eshop.databinding.ActivityShoppingCartDetailsBinding
import mk.ukim.finki.eshop.ui.details.DetailsActivityArgs

class ShoppingCartDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingCartDetailsBinding
    private val args by navArgs<ShoppingCartDetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartDetailsBinding.inflate(layoutInflater)
        binding.shoppingCart = args.shoppingCart
        binding.lifecycleOwner = this
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}