package mk.ukim.finki.eshop.ui.shoppingbag

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import mk.ukim.finki.eshop.api.dto.request.AddProductToBagDto
import mk.ukim.finki.eshop.api.dto.request.RemoveProductFromBagDto
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class ShoppingBagManager @Inject constructor(
    var loginManager: LoginManager,
    var repository: Repository,
    application: Application
): AndroidViewModel(application) {

    suspend fun addProductToShoppingBagForUserSafeCall(body: AddProductToBagDto): NetworkResult<Long> {
        return if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.addProductToBag(body)
                handleAddOrRemoveProductFromShoppingBagResponse(response, body.productId)
            } catch (e: Exception) {
                Log.e("ShoppingBagManager:addProductToShoppingBagForUserSafeCall", "Error adding product to shopping bag for user")
                NetworkResult.Error("Error adding product to shopping bag for user")
            }
        } else {
            Log.e("ShoppingBagManager:addProductToShoppingBagForUserSafeCall", "No internet connection.")
            NetworkResult.Error("No internet connection, please try again.")
        }
    }

    suspend fun removeProductFromShoppingBagForUserSafeCall(body: RemoveProductFromBagDto): NetworkResult<Long> {
        return if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.removeProductFromBag(body)
                handleAddOrRemoveProductFromShoppingBagResponse(response, body.productId)
            } catch (e: Exception) {
                Log.e("ShoppingBagManager:removeProductFromShoppingBagForUserSafeCall", "Error removing product from bag for user")
                NetworkResult.Error("Error removing product from bag for user")
            }
        } else {
            Log.e("ShoppingBagManager:removeProductFromShoppingBagForUserSafeCall", "No internet connection.")
            NetworkResult.Error("No internet connection, please try again.")
        }
    }

    private fun handleAddOrRemoveProductFromShoppingBagResponse(response: Response<Unit>, productId: Long): NetworkResult<Long> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(productId)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }


}