package mk.ukim.finki.eshop.api.model

data class OrderItem(
    val id: Long,
    val price: Float,
    val name: String,
    val sizes: List<Size>,
    val selectedSize: String,
    val selectedQuantity: Int,
    val imageUrl: String,
    val productId: Long
)
