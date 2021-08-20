package mk.ukim.finki.eshop.bindingadapters

import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.Image
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.ui.products.ProductsFragmentDirections
import mk.ukim.finki.eshop.ui.products.ProductsViewModel
import mk.ukim.finki.eshop.ui.wishlist.WishlistFragmentDirections
import mk.ukim.finki.eshop.ui.wishlist.WishlistViewModel
import mk.ukim.finki.eshop.util.Utils
import android.content.res.ColorStateList




class WishlistBinding {
    companion object {

        @BindingAdapter("onWishlistProductClickListener")
        @JvmStatic
        fun onWishlistProductClickListener(productLayout: ConstraintLayout, product: Product) {
            productLayout.setOnClickListener {
                try {
                    val action = WishlistFragmentDirections.actionWishlistFragmentToDetailsActivity(product)
                    productLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.e("onProductClickListener", e.message.toString())
                }
            }
        }

        @BindingAdapter("onTrashClickListener", "setViewModel", requireAll = true)
        @JvmStatic
        fun onTrashClickListener(btn: MaterialButton, product: Product, vm: WishlistViewModel) {
            btn.setOnClickListener {
                    vm.removeProductFromWishlist(product.id)
                    Utils.showSnackbar(btn, "Removed product from wishlist!", Snackbar.LENGTH_SHORT)

            }
        }

        @BindingAdapter("onMoveToBagBtnListener", "setViewModel", requireAll = true)
        @JvmStatic
        fun onMoveToBagBtnListener(btn: MaterialButton, product: Product, vm: WishlistViewModel) {
            val wb = WishlistBinding()
            btn.setOnClickListener {
                if(!product.isInShoppingCart) {
                    wb.setupButtonRemove(btn)
                    vm.moveToBag(product.id)
                    Utils.showSnackbar(btn, "Moved product to shopping bag!", Snackbar.LENGTH_SHORT)
                }
                else {
                    vm.removeFromBag(product.id)
                    wb.setupButtonMoveToBag(btn)
                    Utils.showSnackbar(btn, "Removed product from shopping bag!", Snackbar.LENGTH_SHORT)
                }
                product.isInShoppingCart = !product.isInShoppingCart
            }
        }
        @BindingAdapter("setupButtonStyleWishlist")
        @JvmStatic
        fun setupButtonStyleWishlist(btn: MaterialButton, product: Product) {
            val wb = WishlistBinding()
            if(!product.isInShoppingCart) {
                wb.setupButtonMoveToBag(btn)
            }
            else {
                wb.setupButtonRemove(btn)
            }
        }

    }



    fun setupButtonMoveToBag(btn: MaterialButton) {
        btn.text = "Move to bag"
        btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.green))
    }
    fun setupButtonRemove(btn: MaterialButton) {
        btn.text = "Remove"
        btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.red))
    }
}