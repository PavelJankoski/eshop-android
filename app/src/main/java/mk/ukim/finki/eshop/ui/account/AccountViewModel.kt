package mk.ukim.finki.eshop.ui.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.LoginDto
import mk.ukim.finki.eshop.api.dto.RegisterDto
import mk.ukim.finki.eshop.api.dto.TokenDto
import mk.ukim.finki.eshop.api.model.AuthResponse
import mk.ukim.finki.eshop.data.datastore.DataStoreRepository
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_JWT
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_TYPE
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val loginManager: LoginManager,
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    var authResponse: MutableLiveData<NetworkResult<AuthResponse>> = MutableLiveData()
    var userExistsResponseLogin: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var userExistsResponseRegister: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var registerResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()

    fun checkIfUsernameAlreadyExists(username: String, who: String) {
        if (who.equals("login", true))
            checkIfUsernameAlreadyExistsLogin(username)
        else checkIfUsernameAlreadyExistsRegister(username)
    }

    private fun checkIfUsernameAlreadyExistsLogin(username: String) = viewModelScope.launch {
        userExistsResponseLogin.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                userExistsResponseLogin.value = handleUserExistsResponse(
                    repository.remote.userWithUsernameExist(username)
                )
            }catch (e: Exception) {
                userExistsResponseLogin.value = NetworkResult.Error(
                    "Due to technical problems you can't continue with you action.",
                    false
                )
            }
        }
    }

    private fun checkIfUsernameAlreadyExistsRegister(username: String) = viewModelScope.launch {
        userExistsResponseRegister.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                userExistsResponseRegister.value = handleUserExistsResponse(
                    repository.remote.userWithUsernameExist(username)
                )
            }catch (e: Exception) {
                userExistsResponseRegister.value = NetworkResult.Error(
                    "Due to technical problems you can't continue with you action.",
                    false
                )
            }
        }
    }

    fun login(dto: LoginDto) = viewModelScope.launch {
        authResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                handleResponse(repository.remote.login(dto))
            } catch (e: Exception) {
                authResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    fun loginWithFacebook(dto: TokenDto) = viewModelScope.launch {
        authResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                handleResponse(repository.remote.loginWithFacebook(dto))
            } catch (e: Exception) {
                authResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    fun loginWithGoogle(dto: TokenDto) = viewModelScope.launch {
        authResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                handleResponse(repository.remote.loginWithGoogle(dto))
            } catch (e: Exception) {
                authResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    fun registerUser(dto: RegisterDto) = viewModelScope.launch {
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                repository.remote.registerUser(dto)
                registerResponse.value = NetworkResult.Success(true)
            } catch (e: Exception) {
                Log.e("AccountViewModel: registerUser", "User registration failed")
            }
        }
    }

    private fun handleResponse(response: Response<AuthResponse>) {
        val handledResponse = handleAuthResponse(response)

        if (handledResponse is NetworkResult.Success) {
            handledResponse.data?.getToken()?.let { token ->
                loginManager.saveJwtToken(token)
            }
            handledResponse.data?.getUserId()?.let { userId->
                loginManager.storeUserId(userId)
            }
        }

        authResponse.value = handledResponse
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
                var errorMessage = response.message()
                if (response.errorBody()?.string()?.contains("Invalid password") == true)
                    errorMessage = "Invalid password...."
                NetworkResult.Error(errorMessage)
            }
        }
    }

    private fun handleUserExistsResponse(response: Response<Boolean>): NetworkResult<Boolean> {
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

    fun setGoogleClient(googleApiClient: GoogleSignInClient) {
        loginManager.loginType = GOOGLE_TYPE
        loginManager.googleClient = googleApiClient
    }
}