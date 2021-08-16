package mk.ukim.finki.eshop.ui.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.api.model.ShoppingCart
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_JWT
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_USER_ID
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.log

@ViewModelScoped
class WishlistManager @Inject constructor(
    var loginManager: LoginManager,
    var repository: Repository,
    application: Application
): AndroidViewModel(application) {

    var addOrRemoveProductResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()

    fun addProductToWishlist(productId: Int)  = viewModelScope.launch {
        addOrRemoveProductResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                addOrRemoveProductResponse.value = handleAddProductOrRemoveResponse(
                    repository.remote.addProductToWishlist(userId, productId)
                )
            } catch (e: Exception) {
                addOrRemoveProductResponse.value = NetworkResult.Error("Error getting info....")
            }
        } else {
            addOrRemoveProductResponse.value = NetworkResult.Error("Not connected to the internet. Please check your connection")
        }
    }

    fun removeProductFromWishlist(productId: Int)  = viewModelScope.launch {
        addOrRemoveProductResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                addOrRemoveProductResponse.value = handleAddProductOrRemoveResponse(
                    repository.remote.removeProductFromWishlist(userId, productId)
                )
            } catch (e: Exception) {
                addOrRemoveProductResponse.value = NetworkResult.Error("Error getting info....")
            }
        } else {
            addOrRemoveProductResponse.value = NetworkResult.Error("Not connected to the internet. Please check your connection")
        }
    }


    private fun handleAddProductOrRemoveResponse(response: Response<Void>): NetworkResult<Boolean> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(true)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }



}
