package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val id: Long,
    val street: String,
    val streetNo: String,
    val city: String,
    val country: String,
    val postalCode: Int,
    val isDefault: Boolean
) : Parcelable {
    fun getCityWithPostalCode(): String {
        return postalCode.toString().plus(", ").plus(city)
    }
}
