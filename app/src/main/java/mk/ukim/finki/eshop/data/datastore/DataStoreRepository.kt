package mk.ukim.finki.eshop.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_TOKEN
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_NAME
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_USER_ID
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val jwt = stringPreferencesKey(PREFERENCE_TOKEN)
        val userId = longPreferencesKey(PREFERENCE_USER_ID)
    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCE_NAME
    )


    suspend fun saveJWT(jwt: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.jwt] = jwt
        }
    }

    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.userId] = userId
        }
    }


//    val readUserId: Flow<Long> = context.dataStore.data
//        .map { preferences ->
//            val userId = preferences[PreferenceKeys.userId] ?: DEFAULT_USER_ID.toLong()
//            userId
//        }
//
//    val readJWT: Flow<JwtToken> = context.dataStore.data
//        .map { preferences ->
//            val token = preferences[PreferenceKeys.jwt] ?: DEFAULT_JWT
//            JwtToken(
//                token
//            )
//        }

}

data class JwtToken(
    val token: String
)

data class UserId(
    val userId: Long
)

data class ShoppingCartId(
    val shoppingCartId: Long
)