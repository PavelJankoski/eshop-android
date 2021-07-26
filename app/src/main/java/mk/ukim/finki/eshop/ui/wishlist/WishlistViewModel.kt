package mk.ukim.finki.eshop.ui.wishlist

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.AccountViewModel
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readWishlistProducts: LiveData<List<WishlistEntity>> = repository.local.readWishlistProducts().asLiveData()

    fun deleteProductFromWishlist(id: Int) {
        viewModelScope.launch {
            repository.local.deleteProductFromWishlist(id)
        }
    }

}