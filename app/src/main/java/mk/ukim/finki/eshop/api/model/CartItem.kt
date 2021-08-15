package mk.ukim.finki.eshop.api.model

data class CartItem(
    val id: Int,
    val products: Product,
    val quantity: Int
)