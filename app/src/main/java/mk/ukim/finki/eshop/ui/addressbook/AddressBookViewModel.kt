package mk.ukim.finki.eshop.ui.addressbook

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.model.Address
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddressBookViewModel @Inject constructor(
    private val repository: Repository,
    private val loginManager: LoginManager,
    application: Application
): AndroidViewModel(application) {
    var addressesResponse: MutableLiveData<NetworkResult<List<Address>>> = MutableLiveData()

    fun getAddressesForUser() = viewModelScope.launch {
        getAddressesSafeCall()
    }

    private suspend fun getAddressesSafeCall() {
        addressesResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.getAddressesForUser(loginManager.readUserId())
                addressesResponse.value = handleAddressesResponse(response)
            } catch (e: Exception) {
                Log.e("AddressBookViewModel:getAddressesSafeCall", "Error fetching addresses for user")
                addressesResponse.value = NetworkResult.Error("Error fetching addresses.")
            }
        }
    }

    private fun handleAddressesResponse(response: Response<List<Address>>): NetworkResult<List<Address>> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.body()!!.isNullOrEmpty() -> {
                NetworkResult.Error("Addresses not found.")
            }
            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }
}