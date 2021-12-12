package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val email: String,
    val image: String,
    val name: String,
    val surname: String,
    val phoneNumber: String
) : Parcelable {
    fun fullName(): String {
        return String.format("%s %s", name, surname)
    }
}