package mk.ukim.finki.eshop.api.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Product(
    val id: Long,
    val brand: String,
    val description: String,
    @SerializedName("starRating")
    val rating: Float,
    val numRatings: Int,
    @SerializedName("images")
    val images: @RawValue List<String>,
    val sizes: @RawValue List<Size>,
    val price: Double,
    @SerializedName("code")
    val productCode: String,
    val name: String,
    @SerializedName("isInWishlist")
    var isFavourite: Boolean = false,
    var isInShoppingCart: Boolean = false
) : Parcelable