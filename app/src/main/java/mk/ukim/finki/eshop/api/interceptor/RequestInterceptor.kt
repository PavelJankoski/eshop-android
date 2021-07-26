package mk.ukim.finki.eshop.api.interceptor

import dagger.hilt.android.scopes.ActivityRetainedScoped
import mk.ukim.finki.eshop.data.datastore.DataStoreRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var newRequest: Request = chain.request()

        val token = dataStoreRepository.readJWT

        newRequest = newRequest.newBuilder()
            .addHeader(
                "AccessToken",
                "Bearer $token"
            )
            .build()

        return chain.proceed(newRequest)
    }
}