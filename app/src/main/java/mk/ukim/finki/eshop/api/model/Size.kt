package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Size(
    val id: Long,
    @SerializedName("size")
    val name: String
): Parcelable
