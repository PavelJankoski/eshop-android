package mk.ukim.finki.eshop.ui.wishlist

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.ui.shoppingBag.ShoppingBagManager
import mk.ukim.finki.eshop.util.GlobalVariables
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    private val wishlistManager: WishlistManager,
    private val shoppingBagManager: ShoppingBagManager,
    application: Application
): AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readWishlistProducts: LiveData<List<WishlistEntity>> = repository.local.readWishlistProducts().asLiveData()

    fun deleteProductFromWishlist(id: Int) {
        viewModelScope.launch {
            repository.local.deleteProductFromWishlist(id)
        }
    }

    /** RETROFIT */
    var wishlistProductsResponse: MutableLiveData<NetworkResult<List<Product>>> = MutableLiveData()
    var addProductResponse = shoppingBagManager.addProductToBagResponse
    var removeProductResponse = shoppingBagManager.removeProductFromBagResponse
    var productsInBagNumber = GlobalVariables.productsInBagNumber

    fun getCartItems() = viewModelScope.launch {
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                wishlistProductsResponse.value = handleWishlistResponse(
                    repository.remote.getAllProductsInWishlist(userId)
                )
            } catch (e: Exception) {
                wishlistProductsResponse.value = NetworkResult.Error("Error loading wishlist products...")
            }
        }
    }

    private fun handleWishlistResponse(response: Response<List<Product>>): NetworkResult<List<Product>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
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
                    wishlistProductsResponse.postValue(NetworkResult.Success(products))
                }
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    fun removeProductFromWishlist(id: Int) {
        wishlistProductsResponse.value = NetworkResult.Success(wishlistProductsResponse.value!!.data!!.filter { p -> p.id != id })
        wishlistManager.removeProductFromWishlist(id)
    }

    fun moveToBag(id: Int) {
        shoppingBagManager.addProductToShoppingCart(id)
    }

    fun removeFromBag(id: Int) {
        shoppingBagManager.removeProductFromShoppingCart(id)

    }

}