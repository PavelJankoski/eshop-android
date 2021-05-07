package mk.ukim.finki.eshop.api.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Long,
    @SerializedName("typeString")
    val type: String,
    val imageUrl: String,
    val gender: String
)
