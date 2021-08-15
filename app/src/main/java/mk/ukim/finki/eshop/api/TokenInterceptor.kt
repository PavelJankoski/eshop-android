package mk.ukim.finki.eshop.api

import android.content.Context
import mk.ukim.finki.eshop.data.sharedpreferences.SecureStorage
import mk.ukim.finki.eshop.util.Constants.Companion.PREFERENCE_JSON_WEB_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class TokenInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val token = SecureStorage(context).getString(PREFERENCE_JSON_WEB_TOKEN)
        if(!token.isNullOrBlank()) {
            val requestBuilder: Request.Builder = original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
            return chain.proceed(requestBuilder.build())
        }
        return chain.proceed(original)
    }
}