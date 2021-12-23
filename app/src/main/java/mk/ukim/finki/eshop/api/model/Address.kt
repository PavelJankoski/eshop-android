package mk.ukim.finki.eshop.api.model

data class Address(
    val id: Long,
    val street: String,
    val streetNo: String,
    val city: String,
    val country: String,
    val postalCode: Int,
    val isDefault: Boolean
)
