package mk.ukim.finki.eshop.ui.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.api.dto.request.RegisterDto
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.api.model.User
import mk.ukim.finki.eshop.data.source.Repository
import mk.ukim.finki.eshop.util.Constants.Companion.CLIENT_ID_PARAM
import mk.ukim.finki.eshop.util.Constants.Companion.CLIENT_SECRET_PARAM
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_JWT
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_USER_ID
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
    application: Application
): AndroidViewModel(application) {

    var loginResponse: MutableLiveData<NetworkResult<LoginDto>> = MutableLiveData()
    var userExistsResponseLogin: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var userExistsResponseRegister: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var registerResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    var userDataResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()

    var userId: Long = DEFAULT_USER_ID.toLong()
    var jwt: String = DEFAULT_JWT

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
                handleLoginResponse(repository.remote.loginUser(buildLoginRequestBody(email, password)))
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

//    fun loginWithFacebook(dto: TokenDto) = viewModelScope.launch {
//        authResponse.value = NetworkResult.Loading()
//        if(Utils.hasInternetConnection(getApplication<Application>())) {
//            try {
//                handleLoginResponse(repository.remote.loginWithFacebook(dto))
//            } catch (e: Exception) {
//                authResponse.value = NetworkResult.Error("Cannot authenticate user")
//            }
//        }
//    }

//    fun loginWithGoogle(dto: TokenDto) = viewModelScope.launch {
//        authResponse.value = NetworkResult.Loading()
//        if(Utils.hasInternetConnection(getApplication<Application>())) {
//            try {
//                handleLoginResponse(repository.remote.loginWithGoogle(dto))
//            } catch (e: Exception) {
//                authResponse.value = NetworkResult.Error("Cannot authenticate user")
//            }
//        }
//   }

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

    fun getUserData()  = viewModelScope.launch {
        userDataResponse.value = NetworkResult.Loading()
        if (Utils.hasInternetConnection(getApplication<Application>())) {
            try {
                val userId = loginManager.readUserId()
                userDataResponse.value = handleUserResponse(
                    repository.remote.getUser(userId)
                )
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error("Cannot authenticate user")
            }
        }
    }

    private fun handleUserResponse(response: Response<User>): NetworkResult<User> {
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


    private fun handleLoginResponse(response: Response<LoginDto>): NetworkResult<LoginDto> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                loginManager.saveToken(response.body()!!.token)
                loginManager.saveUserId(response.body()!!.userId)
                //getUserData()
                NetworkResult.Success(response.body()!!)
            }
            else -> {
                NetworkResult.Error(response.message())
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