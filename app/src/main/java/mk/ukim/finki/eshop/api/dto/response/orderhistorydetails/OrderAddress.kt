package mk.ukim.finki.eshop.api.dto.response.orderhistorydetails

data class OrderAddress(
    val street: String,
    val streetNo: String,
    val city: String,
    val country: String,
    val postalCode: Int
) {
    fun getCityWithPostalCode(): String {
        return postalCode.toString().plus(", ").plus(city)
    }
}