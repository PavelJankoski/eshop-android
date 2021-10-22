package mk.ukim.finki.eshop.api.model

import com.google.gson.annotations.SerializedName

data class Category(
    val id: Long,
    @SerializedName("name")
    val type: String,
    val imageUrl: String,
    @SerializedName("type")
    val gender: String
)
