package mk.ukim.finki.eshop.api.dto.response

data class StripePaymentSheet(
    val ephemeralKey: String,
    val paymentIntent: String,
    val customer: String
)
