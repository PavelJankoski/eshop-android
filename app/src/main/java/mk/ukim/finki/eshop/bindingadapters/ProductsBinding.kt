package mk.ukim.finki.eshop.bindingadapters

import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.ui.categories.CategoriesFragmentDirections
import mk.ukim.finki.eshop.ui.products.ProductsFragmentDirections

class ProductsBinding {
    companion object {
        @BindingAdapter("priceToString")
        @JvmStatic
        fun priceToString(tv: TextView, price: Double) {
            tv.text = price.toInt().toString()
        }
        @BindingAdapter("ratingToString")
        @JvmStatic
        fun ratingToString(tv: TextView, rating: Float) {
            tv.text = String.format("%.1f/5", rating)
        }

        @BindingAdapter("onProductClickListener")
        @JvmStatic
        fun onProductClickListener(productLayout: ConstraintLayout, product: Product) {
            productLayout.setOnClickListener {
                try {
                    val action = ProductsFragmentDirections.actionProductsFragmentToDetailsActivity(product)
                    productLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.e("onProductClickListener", e.message.toString())
                }
            }
        }
    }
}