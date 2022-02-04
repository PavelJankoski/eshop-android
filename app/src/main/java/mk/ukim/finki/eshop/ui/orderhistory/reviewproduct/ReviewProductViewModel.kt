package mk.ukim.finki.eshop.ui.orderhistory.reviewproduct

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.request.LeaveReviewDto
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ReviewProductViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    application: Application
) : AndroidViewModel(application) {
    var leaveReviewResponse: MutableLiveData<NetworkResult<Unit>> = MutableLiveData()

    fun leaveReview(productId: Long, review: String, rating: Float) = viewModelScope.launch {
        leaveReviewSafeCall(productId, review, rating)
    }

    private suspend fun leaveReviewSafeCall(productId: Long, review: String, rating: Float) {
        leaveReviewResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.leaveReview(
                    LeaveReviewDto(
                        rating,
                        review,
                        loginManager.readUserId(),
                        productId
                    )
                )
                leaveReviewResponse.value = handleLeaveReviewResponse(response)
            } catch (e: Exception) {
                Log.e(
                    "ReviewProductViewModel:leaveReviewSafeCall",
                    "Error leaving review for product"
                )
                leaveReviewResponse.value = NetworkResult.Error("Error leaving review for product.")
            }
        }
    }

    private fun handleLeaveReviewResponse(response: Response<Unit>): NetworkResult<Unit> {
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