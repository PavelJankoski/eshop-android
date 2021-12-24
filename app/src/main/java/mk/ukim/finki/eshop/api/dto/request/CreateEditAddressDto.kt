package mk.ukim.finki.eshop.api.dto.request

data class CreateEditAddressDto(
    private val street: String,
    private val streetNo: String,
    private val city: String,
    private val country: String,
    private val postalCode: Int,
    private val isDefault: Boolean
)
