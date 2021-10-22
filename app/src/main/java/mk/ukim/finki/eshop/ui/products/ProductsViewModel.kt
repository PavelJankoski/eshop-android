package mk.ukim.finki.eshop.ui.products

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.api.dto.PriceRangeDto
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.ui.wishlist.WishlistManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository,
    val loginManager: LoginManager,
    val shoppingBagManager: ShoppingBagManager,
    private val wishlistManager: WishlistManager,
    application: Application
): AndroidViewModel(application) {

    var listingType: MutableLiveData<Boolean> = MutableLiveData(true)
    var addOrRemoveProductResponse = shoppingBagManager.addOrRemoveProductResponse
    var addProductResponse = shoppingBagManager.addProductToBagResponse
    var removeProductResponse = shoppingBagManager.removeProductFromBagResponse
    var addOrRemovedProduct: Long? = null
    var isRemoveProduct: Boolean? = null


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

    suspend fun isProductInWishlist(id: Int): Boolean {
        return repository.local.isProductInWishlist(id) != 0
    }

    /** RETROFIT */
    var productsResponse: MutableLiveData<NetworkResult<List<Product>>> = MutableLiveData()


    fun getProductsForCategory(categoryId: Long) = viewModelScope.launch {
        getProductsSafeCall(categoryId)
    }

    fun getProductsInPriceRange(dto: PriceRangeDto) = viewModelScope.launch {
        getProductsInPriceRangeSafeCall(dto)
    }

    fun getFilteredProductsForCategory(searchText: String) = viewModelScope.launch {
        getFilteredProductsForCategorySafeCall(searchText)
    }

    fun addProductToShoppingCart(id: Long) {
        addOrRemovedProduct = id
        isRemoveProduct = false
        shoppingBagManager.addProductToShoppingCart(id)
    }

    fun removeProductFromShoppingCart(id: Long) {
        addOrRemovedProduct = id
        isRemoveProduct = true
        shoppingBagManager.removeProductFromShoppingCart(id)
    }


    private suspend fun getProductsSafeCall(categoryId: Long) {
        productsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getProductsByCategory(categoryId, 0)
                productsResponse.value = handleProductsResponse(response)

            } catch (e: Exception) {
                productsResponse.value = NetworkResult.Error("Products not found.")
            }
        }
    }

    private suspend fun getProductsInPriceRangeSafeCall(dto: PriceRangeDto) {
        productsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getFilteredProductsForCategory(dto, 0)
                productsResponse.value = handleProductsResponse(response)

            } catch (e: Exception) {
                productsResponse.value = NetworkResult.Error("Products not found.")
            }
        }
    }

    private suspend fun getFilteredProductsForCategorySafeCall(searchText: String) {
        productsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                withContext(Dispatchers.Main) {
                    val response = repository.remote.getSearchedProducts(searchText, 0)
                    productsResponse.postValue(handleProductsResponse(response))
                }

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
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }


    fun addOrRemoveProductShoppingCart() {
        viewModelScope.launch {
            productsResponse.value?.data!!.find { it.id == addOrRemovedProduct }?.isInShoppingCart = !isRemoveProduct!!
            productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!)
        }
    }

    fun deleteProductFromWishlist(id: Long) {
        productsResponse.value?.data!!.find { it.id == id }?.isFavourite = false
        productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!)
        wishlistManager.removeProductFromWishlist(id)
    }

    fun insertProductInWishlist(product: Product) {
        productsResponse.value?.data!!.find { it.id == product.id }?.isFavourite = true
        productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!)
        wishlistManager.addProductToWishlist(product.id)
    }

}