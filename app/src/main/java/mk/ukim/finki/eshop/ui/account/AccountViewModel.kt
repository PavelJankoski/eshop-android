package mk.ukim.finki.eshop.ui.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.LoginDto
import mk.ukim.finki.eshop.api.dto.RegisterDto
import mk.ukim.finki.eshop.api.dto.TokenDto
import mk.ukim.finki.eshop.api.model.AuthResponse
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {
    var authResponse: MutableLiveData<NetworkResult<AuthResponse>> = MutableLiveData()


    fun login(dto: LoginDto) = viewModelScope.launch {
        authResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.login(dto)
                authResponse.value = handleAuthResponse(response)

            } catch (e: Exception) {
                authResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    fun loginWithFacebook(dto: TokenDto) = viewModelScope.launch {
        authResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.loginWithFacebook(dto)
                authResponse.value = handleAuthResponse(response)

            } catch (e: Exception) {
                authResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    fun registerUser(dto: RegisterDto) = viewModelScope.launch {
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val response = repository.remote.registerUser(dto)
            } catch (e: Exception) {
                Log.e("AccountViewModel: registerUser", "User registration failed")
            }
        }
    }

    private fun handleAuthResponse(response: Response<AuthResponse>): NetworkResult<AuthResponse> {
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