package mk.ukim.finki.eshop.ui.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.request.AddProductToBagDto
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.shoppingbag.ShoppingBagManager
import mk.ukim.finki.eshop.util.NetworkResult
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistManager: WishlistManager,
    private val shoppingBagManager: ShoppingBagManager,
    val loginManager: LoginManager,
    application: Application
): AndroidViewModel(application) {

    /** RETROFIT */
    var wishlistProductsResponse: MutableLiveData<NetworkResult<List<Product>>> = MutableLiveData()
    var removeProductFromWishlistResponse: MutableLiveData<NetworkResult<Long>> = MutableLiveData()
    var addProductToShoppingBagResponse: MutableLiveData<NetworkResult<Long>> = MutableLiveData()

    fun getWishlistProductsForUser() = viewModelScope.launch {
        wishlistProductsResponse.value = NetworkResult.Loading()
        wishlistProductsResponse.value = wishlistManager.getWishlistProductsForUserSafeCall()
    }

    fun removeProductFromWishlistForUser(productId: Long) = viewModelScope.launch {
        removeProductFromWishlistResponse.value = NetworkResult.Loading()
        removeProductFromWishlistResponse.value = wishlistManager.removeProductFromWishlistForUserSafeCall(productId)
    }

    fun moveProductToShoppingBag(productId: Long, sizeId: Long) = viewModelScope.launch {
        val body = AddProductToBagDto(loginManager.readUserId(), productId, sizeId, 1)
        addProductToShoppingBagResponse.value = NetworkResult.Loading()
        addProductToShoppingBagResponse.value = shoppingBagManager.addProductToShoppingBagForUserSafeCall(body)
    }

    fun removeProductFromWishlistAfterResponse(productId: Long) {
        wishlistProductsResponse.value = NetworkResult.Success(wishlistProductsResponse.value?.data!!.filter { p -> p.id != productId })
    }


}