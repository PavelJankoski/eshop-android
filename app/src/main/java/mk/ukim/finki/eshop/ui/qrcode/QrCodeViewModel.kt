package mk.ukim.finki.eshop.ui.qrcode

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val repository: Repository,
    val loginManager: LoginManager,
    application: Application
) : AndroidViewModel(application) {
    var product: MutableLiveData<NetworkResult<Product>> = MutableLiveData()

    fun getProductByCode(productCode: String) = viewModelScope.launch {
        getProductByCodeSafeCall(productCode)
    }

    private suspend fun getProductByCodeSafeCall(productCode: String) {
        product.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getProductByProductCode(productCode, loginManager.readUserId())
                product.value = handleProductsResponse(response, productCode)

            } catch (e: Exception) {
                product.value = NetworkResult.Error("Error during product code scanning")
            }
        }
    }

    private fun handleProductsResponse(response: Response<Product>, productCode: String): NetworkResult<Product>{

        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error("Product with code $productCode not found.")
            }
        }
    }


}