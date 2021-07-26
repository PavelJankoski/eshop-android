package mk.ukim.finki.eshop.ui.account

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mk.ukim.finki.eshop.data.datastore.DataStoreRepository
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_JWT
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_USER_ID
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_TYPE
import javax.inject.Inject
import javax.inject.Singleton

@ActivityRetainedScoped
class LoginManager @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    var loginType = ""
    var googleClient: GoogleSignInClient? = null
    var loggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var token = DEFAULT_JWT

    val readToken = dataStoreRepository.readJWT
    val readUserId = dataStoreRepository.readUserId

    fun logoutUser() {
        saveJwtToken(DEFAULT_JWT)
        storeUserId(DEFAULT_USER_ID.toLong())
        checkAndUpdateLoginState()
        if (loginType.equals(GOOGLE_TYPE, true)) {
            googleClient?.signOut()
        }
    }

    fun checkAndUpdateLoginState() {
        CoroutineScope(IO).launch {
            updateUserState()
        }
    }

    fun saveJwtToken(token: String) {
        CoroutineScope(IO).launch {
            saveToken(token)
        }
    }

    fun storeUserId(userId: Long) {
        CoroutineScope(IO).launch {
            saveUserId(userId)
        }
    }

    private suspend fun saveToken(token: String) {
        dataStoreRepository.saveJWT(token)
        updateUserState()
    }

    private suspend fun saveUserId(userId: Long) {
        dataStoreRepository.saveUserId(userId)
    }

    private suspend fun updateUserState() {
        readToken.collect { value ->
            token = value.token
            withContext(Main) {
               loggedIn.value = (token != DEFAULT_JWT)
            }
        }
    }
}