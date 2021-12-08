package mk.ukim.finki.eshop.data.sharedpreferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import mk.ukim.finki.eshop.util.Constants.Companion.ENCRYPTED_SHARED_PREFERENCES_NAME
import androidx.security.crypto.MasterKey
import androidx.security.crypto.EncryptedSharedPreferences


@Singleton
class SecureStorage @Inject constructor(@ApplicationContext context : Context) {
    private val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        ENCRYPTED_SHARED_PREFERENCES_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val editor = sharedPreferences.edit()

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun clearStorage() {
        editor.clear().apply()
    }

}