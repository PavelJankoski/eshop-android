package mk.ukim.finki.eshop.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.NetworkResult
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    fun deleteProductFromWishlist(id: Int) {
        viewModelScope.launch {
            repository.local.deleteProductFromWishlist(id)
        }
    }

    fun insertProductInWishlist(product: Product) {
        viewModelScope.launch {
            repository.local.insertProductInWishlist(WishlistEntity(product.id, product.brand, product.condition, product.description, product.rating, product.price, product.productCode, product.name))
        }
    }
}