package mk.ukim.finki.eshop.ui.addressbook

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.request.CreateEditAddressDto
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
    var createEditAddressResponse: MutableLiveData<NetworkResult<Address>> = MutableLiveData()

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

    fun createAddressesForUser(body: CreateEditAddressDto) = viewModelScope.launch {
        createEditAddressesSafeCall(body)
    }

    fun editAddressesForUser(addressId: Long, body: CreateEditAddressDto) = viewModelScope.launch {
        createEditAddressesSafeCall(body, addressId)
    }

    private suspend fun createEditAddressesSafeCall(body: CreateEditAddressDto, addressId: Long = 0L) {
        createEditAddressResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response: Response<Address> = if(addressId == 0L) {
                    repository.remote.createAddressForUser(loginManager.readUserId(), body)
                } else {
                    repository.remote.editAddressForUser(addressId, loginManager.readUserId(), body)
                }
                createEditAddressResponse.value = handleCreateEditAddressResponse(response)
            } catch (e: Exception) {
                Log.e("AddressBookViewModel:createEditAddressesSafeCall", "Error saving address for user")
                createEditAddressResponse.value = NetworkResult.Error("Error saving address.")
            }
        }
    }


    private fun handleCreateEditAddressResponse(response: Response<Address>): NetworkResult<Address> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
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