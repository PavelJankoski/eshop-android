package mk.ukim.finki.eshop.ui.search

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.SearchEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.showToast
import java.lang.Exception
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    private val shoppingBagManager: ShoppingBagManager,
    application: Application
) : AndroidViewModel(application) {
    private val mContext = application.applicationContext

    val readSearchHistory: LiveData<List<SearchEntity>> = repository.local.readSearchHistory().asLiveData()
    var product: MutableLiveData<Product> = MutableLiveData()

    fun insertSearchText(text: String) = viewModelScope.launch {
        repository.local.insertSearchText(SearchEntity(text, LocalDateTime.now()))
    }

    fun deleteSearchText(id: Int) = viewModelScope.launch {
        repository.local.deleteSearchText(id)
    }

    fun deleteAllSearchHistory() = viewModelScope.launch {
        repository.local.deleteAllSearchHistory()
    }

    suspend fun getProductByProductCodeSafeCall(productCode: String) {
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getProductByProductCode(productCode)
                if(response.isSuccessful) {
                    val p = response.body()
                    val dto = shoppingBagManager.isInShoppingCartAndFaveSafeCall(p!!.id)
                    if (dto != null) {
                        p.isFavourite = dto.isFavorite.toBoolean()
                        p.isInShoppingCart = dto.isInShoppingCart.toBoolean()
                    }
                    product.value = p
                }
                else {
                    showToast(mContext, "Cannot find product with product code $productCode", Toast.LENGTH_SHORT)
                }
            } catch (e: Exception) {
                showToast(mContext, "Cannot fetch product by product code", Toast.LENGTH_SHORT)
                Log.e("SearchViewModel", "Cannot fetch product by product code")
            }
        }
    }
}
