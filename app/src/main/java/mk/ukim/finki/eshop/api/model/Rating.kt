package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDateTime
import mk.ukim.finki.eshop.api.model.User

@Parcelize
data class Rating(
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("review")
    val review: String,
    @SerializedName("reviewedOn")
    val reviewedOn: String,
    @SerializedName("user")
    val user: @RawValue User
) : Parcelable
