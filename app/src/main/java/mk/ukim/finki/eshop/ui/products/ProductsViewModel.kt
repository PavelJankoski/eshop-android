package mk.ukim.finki.eshop.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {
    var listingType: MutableLiveData<Boolean> = MutableLiveData(true)

    fun changeListing() {
        listingType.value = !listingType.value!!
    }

    fun orderProductsByCriteria(position: Int) {
        if(productsResponse.value?.data != null) {
            when(position) {
                0 -> {
                    productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!.sortedBy { it.description })
                }
                1 -> {
                    productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!.sortedBy { it.price })
                }
                2-> {
                    productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!.sortedByDescending { it.price })
                }
            }
        }

    }

    /** RETROFIT */
    var productsResponse: MutableLiveData<NetworkResult<List<Product>>> = MutableLiveData()


    fun getProductsForCategory(categoryId: Long) = viewModelScope.launch {
        getProductsSafeCall(categoryId)
    }

    private suspend fun getProductsSafeCall(categoryId: Long) {
        productsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getProductsByCategory(categoryId)
                productsResponse.value = handleProductsResponse(response)

            } catch (e: Exception) {
                productsResponse.value = NetworkResult.Error("Products not found.")
            }
        }
    }

    private fun handleProductsResponse(response: Response<List<Product>>): NetworkResult<List<Product>>{
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("Products not found.")
            }
            response.isSuccessful -> {
                val products = response.body()
                NetworkResult.Success(products!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }
}