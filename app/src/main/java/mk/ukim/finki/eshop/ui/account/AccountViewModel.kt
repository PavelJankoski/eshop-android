package mk.ukim.finki.eshop.ui.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.api.dto.request.RegisterDto
import mk.ukim.finki.eshop.api.dto.request.TokenDto
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.api.model.User
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.ui.shoppingbag.ShoppingBagManager
import mk.ukim.finki.eshop.util.Constants.Companion.CLIENT_ID_PARAM
import mk.ukim.finki.eshop.util.Constants.Companion.CLIENT_SECRET_PARAM
import mk.ukim.finki.eshop.util.Constants.Companion.FACEBOOK_TYPE
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_TYPE
import mk.ukim.finki.eshop.util.Constants.Companion.GRANT_TYPE_PARAM
import mk.ukim.finki.eshop.util.Constants.Companion.PASSWORD_PARAM
import mk.ukim.finki.eshop.util.Constants.Companion.USERNAME_PARAM
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val loginManager: LoginManager,
    private val repository: Repository,
    private val shoppingBagManager: ShoppingBagManager,
    application: Application
): AndroidViewModel(application) {

    var loginResponse: MutableLiveData<NetworkResult<LoginDto>> = MutableLiveData()
    var registerResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var userInfoResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()

    private fun buildLoginRequestBody(email: String, password: String): RequestBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(USERNAME_PARAM, email)
            .addFormDataPart(PASSWORD_PARAM, password)
            .addFormDataPart(GRANT_TYPE_PARAM, "password")
            .addFormDataPart(CLIENT_ID_PARAM, "User")
            .addFormDataPart(CLIENT_SECRET_PARAM, "bar")
            .build()
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        loginResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                loginResponse.value = handleLoginResponse(repository.remote.loginUser(buildLoginRequestBody(email, password)))
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    fun getUserInfo() = viewModelScope.launch {
        userInfoResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                userInfoResponse.value = handleGetUserInfoResponse(repository.remote.getUserInfo(loginManager.readUserId()))
            } catch (e: Exception) {
                userInfoResponse.value = NetworkResult.Error("Cannot fetch user info")
            }
        }
    }


    fun loginWithGoogle(dto: TokenDto) = viewModelScope.launch {
        loginResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                loginResponse.value = handleLoginResponse(repository.remote.loginWithGoogle(dto))
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error("Cannot authenticate user")
                Log.e("GOOGLE_AUTH: ", "Cannot authenticate user")
            }
        }
    }

    fun loginWithFacebook(dto: TokenDto) = viewModelScope.launch {
        loginResponse.value = NetworkResult.Loading()
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                setFacebookClient()
                loginResponse.value = handleLoginResponse(repository.remote.loginWithFacebook(dto))
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error("Cannot authenticate user")
                Log.e("FACEBOOK_AUTH: ", "Cannot authenticate user")
            }
        }
    }

    fun checkLoggedInUser() {
        if (MyApplication.loggedIn.value) {
            loginResponse.value = NetworkResult.Success(loginManager.getLoggedInUser())
        }
    }

    fun registerUser(dto: RegisterDto) = viewModelScope.launch {
        if(Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                handleRegisterResponse(repository.remote.registerUser(dto))
            } catch (e: Exception) {
                Log.e("AccountViewModel: registerUser", "User registration failed")
            }
        }
    }

    private fun handleRegisterResponse(response: Response<Void>) {
        if(response.isSuccessful) {
            registerResponse.value = NetworkResult.Success(true)
        }
        else {
            registerResponse.value = NetworkResult.Error("Registration failed. Please try again")
        }
    }


    private fun handleLoginResponse(response: Response<LoginDto>): NetworkResult<LoginDto> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                loginManager.saveUser(response.body()!!)
                getItemsInBagForUser()
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error("Bad credentials. Please try again")
            }
        }
    }

    private fun handleGetUserInfoResponse(response: Response<User>): NetworkResult<User> {
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

    private fun setFacebookClient() {
        loginManager.loginType = FACEBOOK_TYPE
    }

    fun setGoogleClient(googleApiClient: GoogleSignInClient) {
        loginManager.loginType = GOOGLE_TYPE
        loginManager.googleClient = googleApiClient
    }

    fun logout() {
        loginManager.logoutUser()
    }

    private fun getItemsInBagForUser() = viewModelScope.launch {
        shoppingBagManager.getItemsInBagForUserSafeCall()
    }

}