package mk.ukim.finki.eshop.api.model

import kotlinx.parcelize.RawValue
import java.time.LocalDateTime
import mk.ukim.finki.eshop.api.model.User

data class Review(
    val rating: Float,
    val review: String,
    val reviewedOn: String,
    val fullName: String,
    val imageUrl: String?
)
