package mk.ukim.finki.eshop.api.model

import com.google.gson.annotations.SerializedName

data class OrderHistory(
    val totalOrders: Int,
    @SerializedName("orderHistoryItemDtos")
    val orderHistoryItems: List<OrderHistoryItem>
)
