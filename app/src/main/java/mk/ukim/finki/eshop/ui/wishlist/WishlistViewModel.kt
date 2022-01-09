package mk.ukim.finki.eshop.ui.wishlist

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.GlobalVariables
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    private val wishlistManager: WishlistManager,
    application: Application
): AndroidViewModel(application) {

    /** RETROFIT */
    var wishlistProductsResponse: MutableLiveData<NetworkResult<List<Product>>> = MutableLiveData()
    var removeProductFromWishlistResponse: MutableLiveData<NetworkResult<Long>> = MutableLiveData()
    var productsInBagNumber = GlobalVariables.productsInBagNumber

    fun getWishlistProductsForUser() = viewModelScope.launch {
        wishlistProductsResponse.value = NetworkResult.Loading()
        wishlistProductsResponse.value = wishlistManager.getWishlistProductsForUserSafeCall()
    }

    fun removeProductFromWishlistForUser(productId: Long) = viewModelScope.launch {
        removeProductFromWishlistResponse.value = NetworkResult.Loading()
        removeProductFromWishlistResponse.value = wishlistManager.removeProductFromWishlistForUserSafeCall(productId)
    }

    fun removeProductFromWishlistAfterResponse(productId: Long) {
        wishlistProductsResponse.value = NetworkResult.Success(wishlistProductsResponse.value?.data!!.filter { p -> p.id != productId })
    }


}