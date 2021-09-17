package mk.ukim.finki.eshop.api.model

import android.app.AlertDialog
import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize
import mk.ukim.finki.eshop.util.Utils

@Parcelize
data class ShoppingCart(
    val cartStatus: String,
    val id: Long,
    val cartItems: List<CartItem>,
    val createDate: String,
    val endDate: String?
) : Parcelable {
    fun startTimeDisplayString(): String {
        return Utils.shortDateFormat(createDate)
    }

    fun endDateDisplayString(): String {
        if(endDate.isNullOrEmpty()) {
            return ""
        }
        return Utils.shortDateFormat(endDate!!)
    }

    fun cartStatusDescription(): String {
        return when {
            cartStatus.lowercase().equals("canceled") -> {
                "You have canceled you're order."
            }
            cartStatus.lowercase().equals("finished") -> {
                "Successful ordering and payment"
            }
            else -> {
                ""
            }
        }
    }

    fun endDateVisibility(): Int {
        if(endDate.isNullOrEmpty()) {
            return View.GONE
        }
        return View.VISIBLE
    }
}