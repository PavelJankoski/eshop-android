package mk.ukim.finki.eshop.api.model

import com.google.gson.annotations.SerializedName

data class OrderDetails(
    @SerializedName("userInfoDto")
    val userInfo: User,
    @SerializedName("addressDto")
    val address: Address?,
    val totalPrice: Float
)
