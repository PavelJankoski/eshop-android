package mk.ukim.finki.eshop.api.dto

import com.google.gson.annotations.SerializedName

data class FavCartDto (
    @SerializedName(value = "isFavorite")
    val isFavorite: String,
    @SerializedName(value = "isShoppingCart")
    val isInShoppingCart: String
)