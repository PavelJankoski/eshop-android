package mk.ukim.finki.eshop.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class Utils {
    companion object {
        fun hasInternetConnection(application: Application): Boolean {
            val connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        fun showToast(context: Context, message: String, duration: Int) {
            Toast.makeText(context, message, duration).show()
        }
    }
}