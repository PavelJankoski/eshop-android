package mk.ukim.finki.eshop.ui.account

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.scopes.ActivityRetainedScoped
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.MyApplication.Companion.loggedIn
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.data.sharedpreferences.SecureStorage
import mk.ukim.finki.eshop.util.Constants.Companion.FACEBOOK_TYPE
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_TYPE
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_EMAIL
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_TOKEN
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_USER_ID
import javax.inject.Inject

@ActivityRetainedScoped
class LoginManager @Inject constructor(
    private val secureStorage: SecureStorage
) {

    var loginType = ""
    lateinit var googleClient: GoogleSignInClient
    fun logoutUser() {
        secureStorage.clearStorage()
        if (loginType.equals(GOOGLE_TYPE, true)) {
            googleClient.signOut()
        }
        else if(loginType.equals(FACEBOOK_TYPE, true)) {
            com.facebook.login.LoginManager.getInstance().logOut()
        }
        MyApplication.itemsInBag = 0
        loggedIn.value = false
    }
    private fun saveToken(token: String) {
        secureStorage.putString(PREFERENCE_TOKEN, token)
        loggedIn.value = true
    }

    private fun saveUserId(userId: Long) {
        secureStorage.putString(PREFERENCE_USER_ID, userId.toString())
    }


    private fun saveUserEmail(email: String) {
        secureStorage.putString(PREFERENCE_EMAIL, email)
    }

    fun saveUser(user: LoginDto) {
        this.saveToken(user.token)
        this.saveUserId(user.userId)
        this.saveUserEmail(user.email)
    }

    fun readToken(): String {
        val token = secureStorage.getString(PREFERENCE_TOKEN)
        if (token.isNullOrBlank()) return ""
        return "Bearer $token"
    }

    fun readUserId(): Long? {
        val userId = secureStorage.getString(PREFERENCE_USER_ID)
        return if (userId.isNullOrBlank()) {
            null
        } else {
            secureStorage.getString(PREFERENCE_USER_ID)!!.toLong()
        }
    }


    fun readUserEmail(): String {
        return secureStorage.getString(PREFERENCE_EMAIL) ?: ""
    }

    fun getLoggedInUser(): LoginDto {
        return LoginDto(this.readToken(), this.readUserId()!!, this.readUserEmail())
    }

    fun updateAuthState() {
        val token: String = readToken()
        loggedIn.value = token.isNotBlank()
    }

}