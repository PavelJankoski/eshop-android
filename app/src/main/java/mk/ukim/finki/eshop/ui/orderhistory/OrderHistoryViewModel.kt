package mk.ukim.finki.eshop.ui.orderhistory

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.response.orderhistory.OrderHistory
import mk.ukim.finki.eshop.api.dto.response.orderhistorydetails.OrderHistoryDetails
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    application: Application
) : AndroidViewModel(application) {
    var orderHistoryResponse: MutableLiveData<NetworkResult<OrderHistory>> = MutableLiveData()
    var orderHistoryDetailsResponse: MutableLiveData<NetworkResult<OrderHistoryDetails>> =
        MutableLiveData()

    fun getOrderHistoryForUser() = viewModelScope.launch {
        getOrderHistorySafeCall()
    }

    fun getOrderHistoryDetails(orderId: Long) = viewModelScope.launch {
        getOrderHistoryDetailsSafeCall(orderId)
    }

    private suspend fun getOrderHistorySafeCall() {
        orderHistoryResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getOrderHistory(loginManager.readUserId())
                orderHistoryResponse.value = handleOrderHistoryResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "OrderHistoryViewModel:getOrderHistorySafeCall",
                    "Error fetching order history for user"
                )
                orderHistoryResponse.value = NetworkResult.Error("Error fetching order history.")
            }
        }
    }

    private fun handleOrderHistoryResponse(response: Response<OrderHistory>): NetworkResult<OrderHistory> {
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

    private suspend fun getOrderHistoryDetailsSafeCall(orderId: Long) {
        orderHistoryDetailsResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getOrderHistoryDetails(orderId)
                orderHistoryDetailsResponse.value = handleOrderHistoryDetailsResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "OrderHistoryViewModel:getOrderHistoryDetailsSafeCall",
                    "Error fetching order history details"
                )
                orderHistoryResponse.value =
                    NetworkResult.Error("Error fetching order history details.")
            }
        }
    }

    private fun handleOrderHistoryDetailsResponse(response: Response<OrderHistoryDetails>): NetworkResult<OrderHistoryDetails> {
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