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
    private val rating: Float,
    @SerializedName("review")
    private val review: String,
    @SerializedName("reviewedOn")
    private val reviewedOn: String,
    @SerializedName("user")
    private val user: @RawValue User
) : Parcelable
