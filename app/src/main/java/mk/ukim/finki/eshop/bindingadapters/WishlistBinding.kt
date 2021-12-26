package mk.ukim.finki.eshop.bindingadapters

import android.app.AlertDialog
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.ui.wishlist.WishlistFragmentDirections
import mk.ukim.finki.eshop.ui.wishlist.WishlistViewModel
import mk.ukim.finki.eshop.util.Utils


class WishlistBinding {
    companion object {

        @BindingAdapter("onWishlistProductClickListener")
        @JvmStatic
        fun onWishlistProductClickListener(productLayout: ConstraintLayout, product: Product) {
            productLayout.setOnClickListener {
                try {
                    val action =
                        WishlistFragmentDirections.actionWishlistFragmentToDetailsFragment(product)
                    productLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.e("onProductClickListener", e.message.toString())
                }
            }
        }

        @BindingAdapter("onTrashClickListener", "setViewModel", requireAll = true)
        @JvmStatic
        fun onTrashClickListener(btn: MaterialButton, product: Product, vm: WishlistViewModel) {
            btn.setOnClickListener {
                AlertDialog.Builder(btn.context)
                    .setTitle("Delete wishlist product")
                    .setMessage("Are you sure you want to remove this product from wishlist?")
                    .setPositiveButton("Yes") { _, _ ->
                        vm.removeProductFromWishlistForUser(product.id)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }

        @BindingAdapter("onMoveToBagBtnListener", "setViewModel", requireAll = true)
        @JvmStatic
        fun onMoveToBagBtnListener(btn: MaterialButton, product: Product, vm: WishlistViewModel) {
            val sb = SharedBinding()
            btn.setOnClickListener {
                vm.removeFromBag(product.id)
                sb.setupButtonMoveToBag(btn)
            }
        }

    }

}