package mk.ukim.finki.eshop.api.dto.request

data class ChangeQuantityInBagDto(
    val userId: Long,
    val productId: Long,
    val sizeId: Long,
    val quantity: Int
)
