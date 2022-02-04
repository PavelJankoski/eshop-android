package mk.ukim.finki.eshop.api.dto.response.orderhistorydetails

data class OrderProduct(
    val productId: Long,
    val name: String,
    val size: String,
    val price: Float,
    val quantity: Int,
    val imageUrl: String,
    val isReviewed: Boolean
)