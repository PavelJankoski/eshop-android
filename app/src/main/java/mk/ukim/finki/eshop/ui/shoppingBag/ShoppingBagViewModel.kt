package mk.ukim.finki.eshop.ui.shoppingBag

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.api.model.CartItem
import mk.ukim.finki.eshop.api.model.ShoppingCart
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_JWT
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_USER_ID
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class ShoppingBagViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    application: Application
) : AndroidViewModel(application) {

    var activeShoppingCartExistsResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var shoppingCartResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var cartItemsResponse: MutableLiveData<NetworkResult<List<CartItem>>> = MutableLiveData()


    public fun checkIfUserHasActiveShoppingCart() = viewModelScope.launch {
        activeShoppingCartExistsResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                activeShoppingCartExistsResponse.value = handleUserHasActiveShoppingCartResponse(
                    repository.remote.userHasActiveShoppingCart(userId)
                )
            } catch(e: Exception) {
                activeShoppingCartExistsResponse.value = NetworkResult.Error("Error checking existence...")
            }
        } else {
            activeShoppingCartExistsResponse.value = NetworkResult.Error("")
        }
    }

     fun getCartItems() = viewModelScope.launch {
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                cartItemsResponse.value = handleCartItemsResponse(
                    repository.remote.getCartItems(userId)
                )
            } catch (e: Exception) {
                cartItemsResponse.value = NetworkResult.Error("Error loading cart items...")
            }
        }
    }

    private fun getActiveShoppingCart() = viewModelScope.launch {
        shoppingCartResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                shoppingCartResponse.value = handleShoppingCartResponse(
                    repository.remote.getActiveShoppingCart(
                        userId
                    )
                )
            } catch (e: Exception) {
                shoppingCartResponse.value = NetworkResult.Error("Error fetching the shopping cart...")
            }
        }
    }

    private fun handleUserHasActiveShoppingCartResponse(response: Response<Boolean>): NetworkResult<Boolean> {
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

    private fun handleCartItemsResponse(response: Response<List<CartItem>>): NetworkResult<List<CartItem>> {
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

    private fun handleShoppingCartResponse(response: Response<ShoppingCart>): NetworkResult<Boolean> {
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