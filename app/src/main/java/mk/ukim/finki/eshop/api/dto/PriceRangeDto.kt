package mk.ukim.finki.eshop.api.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceRangeDto(
    private val min: Float,
    private val max: Float
) : Parcelable
