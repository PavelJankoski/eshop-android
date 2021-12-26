package mk.ukim.finki.eshop.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.api.model.ShoppingCart
import mk.ukim.finki.eshop.ui.products.ProductsFragmentDirections
import mk.ukim.finki.eshop.ui.products.ProductsViewModel
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagFragmentDirections
import mk.ukim.finki.eshop.util.Utils.Companion.showSnackbar

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
                    val action = ProductsFragmentDirections.actionProductsFragmentToDetailsFragment(product)
                    productLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.e("onProductClickListener", e.message.toString())
                }
            }
        }

        @BindingAdapter("onShoppingCartClickListener")
        @JvmStatic
        fun onShoppingCartClickListener(shoppingCartLayout: ConstraintLayout, shoppingCart: ShoppingCart) {
            shoppingCartLayout.setOnClickListener {
                try {
                    val action = ShoppingBagFragmentDirections.actionShoppingBagFragmentToShoppingCartDetailsActivity(shoppingCart)
                    shoppingCartLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.e("onShoppingCartClickListener", e.message.toString())
                }
            }
        }

        @BindingAdapter("isProductFavourite", "isLoggedIn", requireAll = true)
        @JvmStatic
        fun isProductFavourite(iv: ImageView, isFavourite: Boolean, isLoggedIn: Boolean) {
            if(isLoggedIn) {
                iv.visibility = View.VISIBLE
                iv.setColorFilter(ContextCompat.getColor(iv.context, R.color.black))
                if(isFavourite) {
                    iv.setImageResource(R.drawable.ic_heart_full)
                }
                else {
                    iv.setImageResource(R.drawable.ic_heart)
                }
            }
            else {
                iv.visibility = View.GONE
            }

        }


        @BindingAdapter("onFavouriteClickListener", "setViewModel", requireAll = true)
        @JvmStatic
        fun onFavouriteClickListener(iv: ImageView, product: Product, vm: ProductsViewModel) {
            iv.setOnClickListener {
                if(product.isFavourite) {
                    vm.deleteProductFromWishlist(product.id)
                    showSnackbar(iv, "Removed product from wishlist!", Snackbar.LENGTH_SHORT)
                }
                else {
                    vm.insertProductInWishlist(product)
                    showSnackbar(iv, "Added product to wishlist!", Snackbar.LENGTH_SHORT)
                }
            }
        }

    }
}