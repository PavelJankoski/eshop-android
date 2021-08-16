package mk.ukim.finki.eshop.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.ui.wishlist.WishlistManager
import mk.ukim.finki.eshop.util.NetworkResult
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val shoppingBagManager: ShoppingBagManager,
    private val wishlistManager: WishlistManager,
    application: Application
): AndroidViewModel(application) {

    fun deleteProductFromWishlist(id: Int) {
        wishlistManager.removeProductFromWishlist(id)
    }

    fun insertProductInWishlist(product: Product) {
        wishlistManager.addProductToWishlist(product.id)
    }

    fun addProductToShoppingCart(id: Int) {
        shoppingBagManager.addProductToShoppingCart(id)
    }

    fun removeProductFromShoppingCart(id: Int) {
        shoppingBagManager.removeProductFromShoppingCart(id)
    }
}