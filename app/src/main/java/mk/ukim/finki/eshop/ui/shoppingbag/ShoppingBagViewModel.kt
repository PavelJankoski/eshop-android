package mk.ukim.finki.eshop.ui.shoppingbag

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.request.ChangeQuantityInBagDto
import mk.ukim.finki.eshop.api.dto.request.RemoveProductFromBagDto
import mk.ukim.finki.eshop.api.model.OrderItem
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ShoppingBagViewModel @Inject constructor(
    private val repository: Repository,
    val loginManager: LoginManager,
    private val shoppingBagManager: ShoppingBagManager,
    application: Application
) : AndroidViewModel(application) {
    var orderItemsResponse: MutableLiveData<NetworkResult<List<OrderItem>>> = MutableLiveData()
    var removeProductFromShoppingBagResponse: MutableLiveData<NetworkResult<Long>> =
        MutableLiveData()
    var changeQuantityForProductInBagResponse: MutableLiveData<NetworkResult<Unit>> =
        MutableLiveData()

    fun getOrderItemsForUser() = viewModelScope.launch {
        getOrderItemsSafeCall()
    }

    fun changeQuantityForProductInBag(productId: Long, sizeId: Long, quantity: Int) =
        viewModelScope.launch {
            changeQuantityForProductInBagSafeCall(productId, sizeId, quantity)
        }

    fun removeProductFromShoppingBag(productId: Long, sizeId: Long) = viewModelScope.launch {
        val body = RemoveProductFromBagDto(loginManager.readUserId(), productId, sizeId)
        removeProductFromShoppingBagResponse.value = NetworkResult.Loading()
        removeProductFromShoppingBagResponse.value =
            shoppingBagManager.removeProductFromShoppingBagForUserSafeCall(body)
    }

    private suspend fun changeQuantityForProductInBagSafeCall(
        productId: Long,
        sizeId: Long,
        quantity: Int
    ) {
        changeQuantityForProductInBagResponse.value = NetworkResult.Loading()
        val body = ChangeQuantityInBagDto(loginManager.readUserId(), productId, sizeId, quantity)
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.changeQuantityInBag(body)
                changeQuantityForProductInBagResponse.value = handleChangeQuantityResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "ShoppingBagViewModel:changeQuantityForProductInBagSafeCall",
                    "Error changing quantity for product in bag"
                )
                changeQuantityForProductInBagResponse.value =
                    NetworkResult.Error("Error changing quantity for product in bag.")
            }
        }
    }

    private suspend fun getOrderItemsSafeCall() {
        orderItemsResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getOrderItemsForUser(loginManager.readUserId())
                orderItemsResponse.value = handleOrderItemsResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "ShoppingBagViewModel:getOrderItemsSafeCall",
                    "Error fetching order items for user"
                )
                orderItemsResponse.value = NetworkResult.Error("Error fetching order items.")
            }
        }
    }

    private fun handleOrderItemsResponse(response: Response<List<OrderItem>>): NetworkResult<List<OrderItem>> {
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

    private fun handleChangeQuantityResponse(response: Response<Unit>): NetworkResult<Unit> {
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
}