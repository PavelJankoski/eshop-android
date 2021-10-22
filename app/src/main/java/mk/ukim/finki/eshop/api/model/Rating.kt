package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime
import mk.ukim.finki.eshop.api.model.User

@Parcelize
data class Rating(
    val rating: Float,
    val review: String,
    val reviewedOn: String,
    val user: @RawValue User
) : Parcelable
