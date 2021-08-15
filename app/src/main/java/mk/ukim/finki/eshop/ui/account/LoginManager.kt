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
import mk.ukim.finki.eshop.data.sharedpreferences.SecureStorage
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_JWT
import mk.ukim.finki.eshop.util.Constants.Companion.DEFAULT_USER_ID
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_TYPE
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_JSON_WEB_TOKEN
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_USER_ID
import javax.inject.Inject
import javax.inject.Singleton

@ActivityRetainedScoped
class LoginManager @Inject constructor(
    private val secureStorage: SecureStorage
) {

    var loginType = ""
    var googleClient: GoogleSignInClient? = null
    var loggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun logoutUser() {
        secureStorage.clearStorage()
        if (loginType.equals(GOOGLE_TYPE, true)) {
            googleClient?.signOut()
        }
        loggedIn.value = false
    }
    fun saveToken(token: String) {
        secureStorage.putString(PREFERENCE_JSON_WEB_TOKEN, token)
        loggedIn.value = true
    }

    fun readToken(): String {
        val token = secureStorage.getString(PREFERENCE_JSON_WEB_TOKEN)
        if(token.isNullOrBlank()) return ""
        return "Bearer ${token}"
    }

    fun saveUserId(userId: Long) {
        secureStorage.putString(PREFERENCE_USER_ID, userId.toString())
    }

    fun readUserId(): Long {
        return secureStorage.getString(PREFERENCE_USER_ID)!!.toLong()
    }

    fun updateAuthState() {
        val token: String = readToken()
        loggedIn.value = token.isNotBlank()
    }

}