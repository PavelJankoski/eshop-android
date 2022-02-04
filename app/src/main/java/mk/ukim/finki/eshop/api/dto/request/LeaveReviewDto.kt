package mk.ukim.finki.eshop.api.dto.request

data class LeaveReviewDto(
    val rating: Float,
    val review: String,
    val userId: Long,
    val productId: Long
)
