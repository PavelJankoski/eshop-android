package mk.ukim.finki.eshop.api.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Product(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("starRating")
    val rating: Float,
    @SerializedName("images")
    val images: @RawValue List<Image>?,
    @SerializedName("price")
    val price: Double,
    @SerializedName("productCode")
    val productCode: String,
    @SerializedName("productName")
    val name: String,
    var isFavourite: Boolean = false,
    var isInShoppingCart: Boolean = false
) : Parcelable