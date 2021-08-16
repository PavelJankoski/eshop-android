package mk.ukim.finki.eshop.ui.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.api.dto.FavCartDto
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
    private val loginManager: LoginManager,
    private val shoppingBagManager: ShoppingBagManager,
    private val wishlistManager: WishlistManager,
    application: Application
): AndroidViewModel(application) {

    var listingType: MutableLiveData<Boolean> = MutableLiveData(true)
    var addOrRemoveProductResponse = shoppingBagManager.addOrRemoveProductResponse
    var addOrRemovedProduct: Int? = null
    var isRemoveProduct: Boolean? = null
    var categoryId: Int? = null

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

    fun getFilteredProductsForCategory(categoryId: Long, searchText: String) = viewModelScope.launch {
        getFilteredProductsForCategorySafeCall(categoryId, searchText)
    }

    fun addProductToShoppingCart(id: Int) {
        addOrRemovedProduct = id
        isRemoveProduct = false
        shoppingBagManager.addProductToShoppingCart(id)
    }

    fun removeProductFromShoppingCart(id: Int) {
        addOrRemovedProduct = id
        isRemoveProduct = true
        shoppingBagManager.removeProductFromShoppingCart(id)
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

    private suspend fun getProductsInPriceRangeSafeCall(dto: PriceRangeDto) {
        productsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getProductsInPriceRange(dto)
                productsResponse.value = handleProductsResponse(response)

            } catch (e: Exception) {
                productsResponse.value = NetworkResult.Error("Products not found.")
            }
        }
    }

    private suspend fun getFilteredProductsForCategorySafeCall(categoryId: Long, searchText: String) {
        productsResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                withContext(Dispatchers.Main) {
                    val response = repository.remote.getFilteredProductsForCategory(categoryId, searchText)
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

                    val products = response.body()!!
                viewModelScope.launch(Dispatchers.IO) {
                    products.forEach { p ->

                        val dto = shoppingBagManager.isInShoppingCartAndFaveSafeCall(p.id)
                        if (dto != null) {
                            p.isFavourite = dto.isFavorite.toBoolean()
                            p.isInShoppingCart = dto.isInShoppingCart.toBoolean()
                        }
                }
                    productsResponse.postValue(NetworkResult.Success(products))
                }

                NetworkResult.Success(products)
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

    fun deleteProductFromWishlist(id: Int) {
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