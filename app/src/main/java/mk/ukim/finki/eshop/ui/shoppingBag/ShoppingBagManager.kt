package mk.ukim.finki.eshop.ui.shoppingBag

import android.app.Application
import android.util.Log
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
import mk.ukim.finki.eshop.api.dto.FavCartDto
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
class ShoppingBagManager @Inject constructor(
    var loginManager: LoginManager,
    var repository: Repository,
    application: Application
): AndroidViewModel(application) {

    var addOrRemoveProductResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var addProductResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var removeProductResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()


    fun addProductToShoppingCart(productId: Int)  = viewModelScope.launch {
        addOrRemoveProductResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                addOrRemoveProductResponse.value = handleAddProductResponse(
                    repository.remote.addProductToShoppingCart(userId, productId)
                )
                addProductResponse.value = addOrRemoveProductResponse.value
            } catch (e: Exception) {
                addOrRemoveProductResponse.value = NetworkResult.Error("Error getting info....")
            }
        } else {
            addOrRemoveProductResponse.value = NetworkResult.Error("Not connected to the internet. Please check your connection")
        }
    }

    fun removeProductFromShoppingCart(productId: Int)  = viewModelScope.launch {
        addOrRemoveProductResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                addOrRemoveProductResponse.value = handleRemoveResponse(
                    repository.remote.removeProductFromShoppingCart(userId, productId)
                )
                removeProductResponse.value = addOrRemoveProductResponse.value
            } catch (e: Exception) {
                addOrRemoveProductResponse.value = NetworkResult.Error("Error getting info....")
            }
        } else {
            addOrRemoveProductResponse.value = NetworkResult.Error("Not connected to the internet. Please check your connection")
        }
    }

    private fun handleIsProductInShoppingCartResponse(response: Response<Boolean>): NetworkResult<Boolean> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleAddProductResponse(response: Response<ShoppingCart>): NetworkResult<Boolean> {
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

    private fun handleRemoveResponse(response: Response<ShoppingCart>): NetworkResult<Boolean> {
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

    suspend fun isInShoppingCartAndFaveSafeCall(productId: Int): FavCartDto? {
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                val response = repository.remote.isInCartAndFave(userId, productId)
                if (response.isSuccessful)
                    return response.body()!!

            } catch (e: java.lang.Exception) {
                Log.e("productViewModel", "is fav and shopping cart failed...")
            }
        }
        return null
    }



}
