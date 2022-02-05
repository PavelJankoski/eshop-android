package mk.ukim.finki.eshop.api

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.data.sharedpreferences.SecureStorage
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(@ApplicationContext private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val token = SecureStorage(context).getString(PREFERENCE_TOKEN)
        if(!token.isNullOrBlank()) {
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
            val response = chain.proceed(requestBuilder.build())
            return if (response.code() != 401) {
                response
            } else {
                MyApplication.loggedIn.value = false
                response.close()
                chain.proceed(original)
            }
        }
        return chain.proceed(original)
    }
}