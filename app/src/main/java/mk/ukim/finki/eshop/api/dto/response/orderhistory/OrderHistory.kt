package mk.ukim.finki.eshop.api.dto.response.orderhistory

import com.google.gson.annotations.SerializedName

data class OrderHistory(
    val totalOrders: Int,
    @SerializedName("orderHistoryItemDtos")
    val orderHistoryItems: List<OrderHistoryItem>
)
