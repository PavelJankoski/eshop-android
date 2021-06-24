package mk.ukim.finki.eshop.ui.categories

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: Repository,
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

    private fun offlineCacheCategories(categories: List<Category>) {
        val categoriesEntity = CategoriesEntity(categories)
        insertCategories(categoriesEntity)
    }

}