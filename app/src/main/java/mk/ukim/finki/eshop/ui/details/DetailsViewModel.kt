package mk.ukim.finki.eshop.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.ui.wishlist.WishlistManager
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository,
    private val shoppingBagManager: ShoppingBagManager,
    val loginManager: LoginManager,
    private val wishlistManager: WishlistManager,
    application: Application
): AndroidViewModel(application) {

    var addProductToBagResponse = shoppingBagManager.addProductToBagResponse
    var removeProductFromBagResponse = shoppingBagManager.removeProductFromBagResponse

    var addProductToWishlistResponse = wishlistManager.addProductToWishlistResponse
    var removeProductFromWishlistResponse = wishlistManager.removeProductFromWishlistResponse


    fun deleteProductFromWishlist(id: Int) {
        wishlistManager.removeProductFromWishlist(id)
    }

    fun insertProductInWishlist(product: Product) {
        wishlistManager.addProductToWishlist(product.id)
    }

    fun addProductToShoppingCart(id: Int, price: Int) {
        shoppingBagManager.addProductToShoppingCart(id, price)
    }

    fun removeProductFromShoppingCart(id: Int, price: Int) {
        shoppingBagManager.removeProductFromShoppingCart(id, price)
    }
}