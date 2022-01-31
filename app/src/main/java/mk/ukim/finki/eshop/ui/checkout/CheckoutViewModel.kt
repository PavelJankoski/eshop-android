package mk.ukim.finki.eshop.ui.checkout

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun getOrderDetailsForUser() = viewModelScope.launch {
        getOrderDetailsForUserSafeCall()
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

    private fun handleOrderDetailsResponse(response: Response<OrderDetails>): NetworkResult<OrderDetails> {
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