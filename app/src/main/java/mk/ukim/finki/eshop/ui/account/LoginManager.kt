package mk.ukim.finki.eshop.ui.account

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import mk.ukim.finki.eshop.api.dto.response.LoginDto
import mk.ukim.finki.eshop.data.sharedpreferences.SecureStorage
import mk.ukim.finki.eshop.util.Constants.Companion.GOOGLE_TYPE
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_EMAIL
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_FULL_NAME
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_IMAGE_URL
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_TOKEN
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_USER_ID
import mk.ukim.finki.eshop.util.GlobalVariables
import javax.inject.Inject

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
        GlobalVariables.productsInBagNumber.value = 0
        loggedIn.value = false
    }
    private fun saveToken(token: String) {
        secureStorage.putString(PREFERENCE_TOKEN, token)
        loggedIn.value = true
    }

    private fun saveUserId(userId: Long) {
        secureStorage.putString(PREFERENCE_USER_ID, userId.toString())
    }

    private fun saveUserFullName(fullName: String) {
        secureStorage.putString(PREFERENCE_FULL_NAME, fullName)
    }

    private fun saveUserEmail(email: String) {
        secureStorage.putString(PREFERENCE_EMAIL, email)
    }

    private fun saveUserImageUrl(imageUrl: String) {
        secureStorage.putString(PREFERENCE_IMAGE_URL, imageUrl)
    }

    fun saveUser(user: LoginDto) {
        this.saveToken(user.token)
        this.saveUserId(user.userId)
        this.saveUserFullName(user.fullName)
        this.saveUserEmail(user.email)
        this.saveUserImageUrl(user.imageUrl)
    }

    fun readToken(): String {
        val token = secureStorage.getString(PREFERENCE_TOKEN)
        if(token.isNullOrBlank()) return ""
        return "Bearer ${token}"
    }

    fun readUserId(): Long {
        return secureStorage.getString(PREFERENCE_USER_ID)!!.toLong()
    }

    fun readUserFullName(): String
    {
        return secureStorage.getString(PREFERENCE_FULL_NAME) ?: ""
    }

    fun readUserEmail(): String
    {
        return secureStorage.getString(PREFERENCE_EMAIL) ?: ""
    }

    fun readUserImageUrl(): String
    {
        return secureStorage.getString(PREFERENCE_IMAGE_URL) ?: ""
    }

    fun getLoggedInUser(): LoginDto {
        return LoginDto(this.readToken(), this.readUserFullName(), this.readUserId(), this.readUserEmail(), this.readUserImageUrl())
    }

    fun updateAuthState() {
        val token: String = readToken()
        loggedIn.value = token.isNotBlank()
    }

}