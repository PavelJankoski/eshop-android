package mk.ukim.finki.eshop.api.model

data class OrderHistoryItem(
    val deliveredOn: String,
    val orderId: Long,
    val imageUrls: List<String>
)
