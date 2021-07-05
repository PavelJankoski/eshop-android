package mk.ukim.finki.eshop.bindingadapters

import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.Image
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.ui.products.ProductsFragmentDirections
import mk.ukim.finki.eshop.ui.products.ProductsViewModel
import mk.ukim.finki.eshop.ui.wishlist.WishlistFragmentDirections
import mk.ukim.finki.eshop.ui.wishlist.WishlistViewModel
import mk.ukim.finki.eshop.util.Utils

class WishlistBinding {
    companion object {

        @BindingAdapter("onWishlistEntityClickListener")
        @JvmStatic
        fun onWishlistEntityClickListener(productLayout: ConstraintLayout, wishlistEntity: WishlistEntity) {
            productLayout.setOnClickListener {
                try {
                    val imagesSplitted : List<Image> = wishlistEntity.images?.split(";")!!.map { Image(0, it) }
                    val product = Product(wishlistEntity.brand, wishlistEntity.condition, wishlistEntity.description, wishlistEntity.id, wishlistEntity.rating, imagesSplitted, wishlistEntity.price, wishlistEntity.productCode, wishlistEntity.name, true)
                    val action = WishlistFragmentDirections.actionWishlistFragmentToDetailsActivity(product)
                    productLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.e("onProductClickListener", e.message.toString())
                }
            }
        }

        @BindingAdapter("loadWishlistImageFromUrl")
        @JvmStatic
        fun loadWishlistImageFromUrl(imageView: ImageView, imageUrlsJoined: String) {
            imageView.load(imageUrlsJoined.split(";")[0]) {
                crossfade(600)
                error(R.drawable.ic_placeholder_image)
            }
        }

        @BindingAdapter("onTrashClickListener", "setViewModel", requireAll = true)
        @JvmStatic
        fun onTrashClickListener(btn: MaterialButton, product: WishlistEntity, vm: WishlistViewModel) {
            btn.setOnClickListener {
                    vm.deleteProductFromWishlist(product.id)
                    Utils.showSnackbar(btn, "Removed product from wishlist!", Snackbar.LENGTH_SHORT)

            }
        }
    }
}