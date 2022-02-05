package mk.ukim.finki.eshop.api.dto.response.orderhistory

data class OrderHistoryItem(
    val deliveredOn: String,
    val orderId: Long,
    val imageUrls: List<String>
)
