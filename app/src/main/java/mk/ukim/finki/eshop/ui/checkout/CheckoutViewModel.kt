package mk.ukim.finki.eshop.ui.checkout

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.response.StripePaymentSheet
import mk.ukim.finki.eshop.api.model.OrderDetails
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    application: Application
) : AndroidViewModel(application) {
    var orderDetailsResponse: MutableLiveData<NetworkResult<OrderDetails>> = MutableLiveData()
    var paymentSheetParamsResponse: MutableLiveData<NetworkResult<StripePaymentSheet>> =
        MutableLiveData()
    var placeOrderResponse: MutableLiveData<NetworkResult<Unit>> = MutableLiveData()


    fun getOrderDetailsForUser() = viewModelScope.launch {
        getOrderDetailsForUserSafeCall()
    }

    private fun getStripePaymentSheetParams(amount: Float) = viewModelScope.launch {
        getStripePaymentSheetParamsSafeCall(amount)
    }

    fun placeOrder() = viewModelScope.launch {
        placeOrderSafeCall()
    }

    private suspend fun placeOrderSafeCall() {
        placeOrderResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.placeOrder(loginManager.readUserId())
                placeOrderResponse.value = handlePlaceOrderResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "CheckoutViewModel:placeOrderSafeCall",
                    "Error placing order for user"
                )
                placeOrderResponse.value =
                    NetworkResult.Error("Error placing order for user.")
            }
        }
    }

    private suspend fun getOrderDetailsForUserSafeCall() {
        orderDetailsResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getOrderDetailsForUser(loginManager.readUserId())
                orderDetailsResponse.value = handleOrderDetailsResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "CheckoutViewModel:getOrderDetailsForUserSafeCall",
                    "Error fetching order details for user"
                )
                orderDetailsResponse.value =
                    NetworkResult.Error("Error fetching order details for user.")
            }
        }
    }

    private suspend fun getStripePaymentSheetParamsSafeCall(amount: Float) {
        paymentSheetParamsResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getPaymentSheetParams(amount)
                paymentSheetParamsResponse.value = handlePaymentSheetParamsResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "CheckoutViewModel:getStripePaymentSheetParamsSafeCall",
                    "Error fetching payment sheet params"
                )
                paymentSheetParamsResponse.value =
                    NetworkResult.Error("Error fetching payment sheet params.")
            }
        }
    }

    private fun handleOrderDetailsResponse(response: Response<OrderDetails>): NetworkResult<OrderDetails> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                getStripePaymentSheetParams(response.body()!!.totalPrice)
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handlePaymentSheetParamsResponse(response: Response<StripePaymentSheet>): NetworkResult<StripePaymentSheet> {
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

    private fun handlePlaceOrderResponse(response: Response<Unit>): NetworkResult<Unit> {
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