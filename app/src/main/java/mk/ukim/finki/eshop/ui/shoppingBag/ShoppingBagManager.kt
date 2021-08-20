package mk.ukim.finki.eshop.ui.shoppingBag

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.api.dto.FavCartDto
import mk.ukim.finki.eshop.api.model.ShoppingCart
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.GlobalVariables
import mk.ukim.finki.eshop.util.GlobalVariables.Companion.productsInBagNumber
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
class ShoppingBagManager @Inject constructor(
    var loginManager: LoginManager,
    var repository: Repository,
    application: Application,
): AndroidViewModel(application) {

    var addOrRemoveProductResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var addProductToBagResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData(NetworkResult.Error("", false))
    var removeProductFromBagResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData(NetworkResult.Error("", false))


    fun addProductToShoppingCart(productId: Int)  = viewModelScope.launch {
        addOrRemoveProductResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                addOrRemoveProductResponse.value = handleAddProductResponse(
                    repository.remote.addProductToShoppingCart(userId, productId)
                )
                addProductToBagResponse.value = addOrRemoveProductResponse.value
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
                removeProductFromBagResponse.value = addOrRemoveProductResponse.value
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
                NetworkResult.Error("Timeout", false)
            }
            response.isSuccessful -> {
                productsInBagNumber.value = productsInBagNumber.value!! + 1
                NetworkResult.Success(true)
            }
            else -> {
                NetworkResult.Error(response.message(), false)
            }
        }
    }

    private fun handleRemoveResponse(response: Response<ShoppingCart>): NetworkResult<Boolean> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout", false)
            }
            response.isSuccessful -> {
                productsInBagNumber.value = productsInBagNumber.value!! - 1
                NetworkResult.Success(true)
            }
            else -> {
                NetworkResult.Error(response.message(), false)
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
