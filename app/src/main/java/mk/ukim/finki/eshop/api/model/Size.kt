package mk.ukim.finki.eshop.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Size(
    val id: Long,
    val name: String
): Parcelable
