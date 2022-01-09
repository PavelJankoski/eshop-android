package mk.ukim.finki.eshop.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.wishlist.WishlistManager
import mk.ukim.finki.eshop.util.NetworkResult
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository,
    val loginManager: LoginManager,
    private val wishlistManager: WishlistManager,
    application: Application
): AndroidViewModel(application) {

    var addProductToWishlistResponse: MutableLiveData<NetworkResult<Long>> = MutableLiveData()
    var removeProductFromWishlistResponse: MutableLiveData<NetworkResult<Long>> = MutableLiveData()

    fun addProductToWishlistForUser(productId: Long) = viewModelScope.launch {
        addProductToWishlistResponse.value = NetworkResult.Loading()
        addProductToWishlistResponse.value = wishlistManager.addProductToWishlistForUserSafeCall(productId)
    }

    fun removeProductFromWishlistForUser(productId: Long) = viewModelScope.launch {
        removeProductFromWishlistResponse.value = NetworkResult.Loading()
        removeProductFromWishlistResponse.value = wishlistManager.removeProductFromWishlistForUserSafeCall(productId)
    }

}