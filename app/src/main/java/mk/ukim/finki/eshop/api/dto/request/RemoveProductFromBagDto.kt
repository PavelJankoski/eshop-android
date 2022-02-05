package mk.ukim.finki.eshop.api.dto.request

data class RemoveProductFromBagDto(
    val userId: Long?,
    val productId: Long,
    val sizeId: Long
)
