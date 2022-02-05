package mk.ukim.finki.eshop.util

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import mk.ukim.finki.eshop.R


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

        fun setupCartItemsBadge(tv: TextView, itemsInCart: Int) {
            if (itemsInCart <= 0) {
                if (tv.visibility != View.GONE) {
                    tv.visibility = View.GONE
                }
            } else {
                tv.text = itemsInCart.toString()
                if (tv.visibility != View.VISIBLE) {
                    tv.visibility = View.VISIBLE
                }
            }
        }

        fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
            var cursor: Cursor? = null
            if (contentUri != null) {
                return try {
                    val proj = arrayOf(MediaStore.Images.Media.DATA)
                    cursor = context.contentResolver.query(contentUri, proj, null, null, null)
                    val columnIndex: Int =
                        cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    cursor.getString(columnIndex)
                } finally {
                    cursor?.close()
                }
            }
            return null
        }
    }
}