package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CartItem(
    val id: Int,
    @SerializedName("product")
    val product: @RawValue Product,
    val quantity: Int
) : Parcelable