package mk.ukim.finki.eshop.ui.wishlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class WishlistManager @Inject constructor(
    var loginManager: LoginManager,
    var repository: Repository,
    application: Application
): AndroidViewModel(application) {

    suspend fun getWishlistProductsForUserSafeCall(): NetworkResult<List<Product>> {
        return if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getWishlistProductsForUser(loginManager.readUserId())
                handleWishlistProductsResponse(response)
            } catch (e: Exception) {
                Log.e("WishlistManager:getWishlistProductsForUserSafeCall", "Error fetching wishlist products for user")
                NetworkResult.Error("Error fetching wishlist products.")
            }
        } else {
            Log.e("WishlistManager:getWishlistProductsForUserSafeCall", "No internet connection.")
            NetworkResult.Error("No internet connection, please try again.")
        }
    }

    private fun handleWishlistProductsResponse(response: Response<List<Product>>): NetworkResult<List<Product>> {
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

    suspend fun addProductToWishlistForUserSafeCall(productId: Long): NetworkResult<Long> {
        return if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.addProductToWishlistForUser(productId, loginManager.readUserId())
                handleAddOrRemoveProductFromWishlistResponse(response)
            } catch (e: Exception) {
                Log.e("WishlistManager:addProductToWishlistForUserSafeCall", "Error adding product to wishlist for user")
                NetworkResult.Error("Error adding product to wishlist for user")
            }
        } else {
            Log.e("WishlistManager:addProductToWishlistForUserSafeCall", "No internet connection.")
            NetworkResult.Error("No internet connection, please try again.")
        }
    }

    suspend fun removeProductFromWishlistForUserSafeCall(productId: Long): NetworkResult<Long> {
        return if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.removeProductFromWishlistForUser(productId, loginManager.readUserId())
                handleAddOrRemoveProductFromWishlistResponse(response)
            } catch (e: Exception) {
                Log.e("WishlistManager:removeProductFromWishlistForUserSafeCall", "Error removing product from wishlist for user")
                NetworkResult.Error("Error removing product from wishlist for user")
            }
        } else {
            Log.e("WishlistManager:removeProductFromWishlistForUserSafeCall", "No internet connection.")
            NetworkResult.Error("No internet connection, please try again.")
        }
    }

    private fun handleAddOrRemoveProductFromWishlistResponse(response: Response<Long>): NetworkResult<Long> {
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
