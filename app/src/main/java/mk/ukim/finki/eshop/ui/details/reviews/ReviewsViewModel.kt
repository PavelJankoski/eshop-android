package mk.ukim.finki.eshop.ui.details.reviews

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Address
import mk.ukim.finki.eshop.api.model.Review
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {
    var reviewsResponse: MutableLiveData<NetworkResult<List<Review>>> = MutableLiveData()

    fun getReviewsForProduct(productId: Long) = viewModelScope.launch {
        getReviewsSafeCall(productId)
    }

    fun getReviewsByRatingForProduct(productId: Long, rating: Float) = viewModelScope.launch {
        getReviewsSafeCall(productId, rating)
    }

    private suspend fun getReviewsSafeCall(productId: Long, rating: Float = 0f) {
        reviewsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = if(rating == 0f) {
                    repository.remote.getReviewsForProduct(productId)
                } else {
                    repository.remote.getReviewsByRatingForProduct(productId, rating)
                }
                reviewsResponse.value = handleReviewsResponse(response)
            } catch (e: Exception) {
                Log.e("ReviewsViewModel:getReviewsSafeCall", "Error fetching reviews for product")
                reviewsResponse.value = NetworkResult.Error("Error fetching reviews.")
            }
        }
    }

    private fun handleReviewsResponse(response: Response<List<Review>>): NetworkResult<List<Review>> {
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