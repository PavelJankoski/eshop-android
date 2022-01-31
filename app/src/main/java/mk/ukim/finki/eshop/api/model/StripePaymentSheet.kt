package mk.ukim.finki.eshop.api.model

data class StripePaymentSheet(
    val ephemeralKey: String,
    val paymentIntent: String,
    val customer: String
)
