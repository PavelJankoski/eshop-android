package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val createDate: String,
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val surname: String,
    val userName: String
) : Parcelable {
    fun fullName(): String {
        return String.format("%s %s", name, surname)
    }
}