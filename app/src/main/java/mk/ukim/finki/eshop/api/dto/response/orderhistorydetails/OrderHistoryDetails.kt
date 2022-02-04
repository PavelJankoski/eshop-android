package mk.ukim.finki.eshop.api.dto.response.orderhistorydetails

import com.google.gson.annotations.SerializedName

data class OrderHistoryDetails(
    val orderId: Long,
    val date: String,
    @SerializedName("addressDto")
    val address: OrderAddress,
    val orderProducts: List<OrderProduct>,
    val totalPrice: Float
)
