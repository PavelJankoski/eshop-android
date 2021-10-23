package mk.ukim.finki.eshop.api.dto.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterProductDto(
    private val categoryId: Long,
    private val min: Float,
    private val max: Float
) : Parcelable
