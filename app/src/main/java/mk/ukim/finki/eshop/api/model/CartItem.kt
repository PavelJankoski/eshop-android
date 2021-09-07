package mk.ukim.finki.eshop.api.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    val id: Int,
    @SerializedName("product")
    val product: Product,
    val quantity: Int
)