package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Int,
    @SerializedName("products")
    val product: Product,
    val quantity: Int
) : Parcelable