package mk.ukim.finki.eshop.util

import android.app.Application
import android.app.Notification
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import mk.ukim.finki.eshop.R
import java.time.LocalDateTime
import java.util.*

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

        fun showShimmerEffect(shimmerFrameLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
            shimmerFrameLayout.startShimmer()
            shimmerFrameLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }

        fun hideShimmerEffect(shimmerFrameLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
            if(shimmerFrameLayout.isShimmerVisible) {
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
            }
            recyclerView.visibility = View.VISIBLE
        }

        fun showToast(context: Context, message: String, duration: Int) {
            Toast.makeText(context, message, duration).show()
        }

        fun showSnackbar(view: View, message: String, duration: Int) {
            Snackbar.make(view, message, duration).setAction("Okay") {}.setActionTextColor(ContextCompat.getColor(view.context, R.color.white)).show()
        }

    }
}