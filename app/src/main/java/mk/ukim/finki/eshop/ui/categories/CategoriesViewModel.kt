package mk.ukim.finki.eshop.ui.categories

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.api.model.CartItem
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.util.GlobalVariables.Companion.productsInBagNumber
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    private val shoppingBagManager: ShoppingBagManager,
    application: Application
): AndroidViewModel(application) {


    /** ROOM DATABASE */
    val readCategories: LiveData<List<CategoriesEntity>> = repository.local.readCategories().asLiveData()

    private fun insertCategories(categoriesEntity: CategoriesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertCategories(categoriesEntity)
        }


    /** RETROFIT */
    var categoriesResponse: MutableLiveData<NetworkResult<List<Category>>> = MutableLiveData()

    fun getCategories() = viewModelScope.launch {
        getCategoriesSafeCall()
    }

    private suspend fun getCategoriesSafeCall() {
        categoriesResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getCategories()
                categoriesResponse.value = handleCategoriesResponse(response)
                val categories = categoriesResponse.value!!.data
                if(categories != null) {
                    offlineCacheCategories(categories)
                }
            } catch (e: Exception) {
                categoriesResponse.value = NetworkResult.Error("Categories not found.")
            }
        }
    }

    private fun handleCategoriesResponse(response: Response<List<Category>>): NetworkResult<List<Category>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("Categories not found.")
            }
            response.isSuccessful -> {
                val categories = response.body()
                NetworkResult.Success(categories!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    fun getCartItems() = viewModelScope.launch {
        if (Utils.hasInternetConnection(getApplication<Application>()) && productsInBagNumber.value == 0) {
            try {
                val userId = loginManager.readUserId()
                val response = repository.remote.getCartItems(userId)
                if(response.isSuccessful) {
                    productsInBagNumber.value = response.body()!!.size
                }

            } catch (e: java.lang.Exception) {
                Log.e("CategoriesViewModel","Error loading cart items...")
            }
        }
    }


    private fun offlineCacheCategories(categories: List<Category>) {
        val categoriesEntity = CategoriesEntity(categories)
        insertCategories(categoriesEntity)
    }

}