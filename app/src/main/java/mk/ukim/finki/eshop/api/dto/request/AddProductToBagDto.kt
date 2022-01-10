package mk.ukim.finki.eshop.api.dto.request

data class AddProductToBagDto(
    val userId: Long,
    val productId: Long,
    val sizeId: Long,
    val quantity: Int
)
