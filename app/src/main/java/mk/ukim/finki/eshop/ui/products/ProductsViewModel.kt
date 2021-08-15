package mk.ukim.finki.eshop.ui.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.FavCartDto
import mk.ukim.finki.eshop.api.dto.PriceRangeDto
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    private val shoppingBagManager: ShoppingBagManager,
    application: Application
): AndroidViewModel(application) {

    var listingType: MutableLiveData<Boolean> = MutableLiveData(true)
    var addOrRemoveProductResponse = shoppingBagManager.addOrRemoveProductResponse
    var addOrRemovedProduct: Int? = null
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
        shoppingBagManager.addProductToShoppingCart(id)
    }

    fun removeProductFromShoppingCart(id: Int) {
        addOrRemovedProduct = id
        shoppingBagManager.removeProductToShoppingCart(id)
    }

    private suspend fun isInShoppingCartAndFaSafeCall(productId: Int): FavCartDto? {
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.isInCartAndFave(userId, productId, token)
                if (response.isSuccessful)
                    return response.body()!!

            } catch (e: Exception) {
                Log.e("productViewModel", "is fav and shopping cart failed...")
            }
        }
        return null
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
                val response = repository.remote.getFilteredProductsForCategory(categoryId, searchText)
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
                products!!.forEach{p ->
                    viewModelScope.launch(Dispatchers.IO) {
                        val dto = isInShoppingCartAndFaSafeCall(p.id)
                        if (dto != null) {
                            p.isFavourite = dto.isFavorite
                            p.isInShoppingCart = dto.isInShoppingCart
                        }
                    }
                }
                NetworkResult.Success(products)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }


    fun addOrProductToShoppingCart(isInShoppingCart: Boolean) {
        viewModelScope.launch {
            productsResponse.value?.data!!.find { it.id == addOrRemovedProduct }?.isInShoppingCart = isInShoppingCart
            productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!)
        }
    }

    fun deleteProductFromWishlist(id: Int) {
        viewModelScope.launch {
            productsResponse.value?.data!!.find { it.id == id }?.isFavourite = false
            productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!)
            repository.local.deleteProductFromWishlist(id)
        }
    }

    fun insertProductInWishlist(product: Product) {
        viewModelScope.launch {
            productsResponse.value?.data!!.find { it.id == product.id }?.isFavourite = true
            productsResponse.value = NetworkResult.Success(productsResponse.value?.data!!)
            val imagesJoined = product.images?.map{it.imageUrl}?.joinToString(";")
            repository.local.insertProductInWishlist(WishlistEntity(product.id, LocalDateTime.now(), product.brand, product.condition, product.description, product.rating, product.price, product.productCode, product.name, imagesJoined))
        }
    }

    private var readJwt = false
    private var readUserId = false

    private var readJWT = loginManager.readToken
    private var readUserID = loginManager.readUserId

    private var token: String = Constants.DEFAULT_JWT
    private var userId: Long = Constants.DEFAULT_USER_ID.toLong()

    fun syncUserAuthData() {
        syncToken()
        syncUserId()
    }

    private fun syncToken() {
        CoroutineScope(Dispatchers.IO).launch {
            readJsonWebToken()
        }
    }

    private fun syncUserId() {

        CoroutineScope(Dispatchers.IO).launch {
            readUserIdToken()
        }
    }

    private suspend fun readJsonWebToken() {
        readJWT.collect { value ->
            token = "Bearer ${value.token}"
            readJwt = (value.token != Constants.DEFAULT_JWT)
            if (readJwt && readUserId) {

            }
        }
    }

    private suspend fun readUserIdToken() {
        readUserID.collect { value ->
            userId = value
            readUserId = (value != Constants.DEFAULT_USER_ID.toLong())
            if (readJwt && readUserId) {

            }
        }
    }
}