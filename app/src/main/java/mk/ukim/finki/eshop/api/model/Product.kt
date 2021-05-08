package mk.ukim.finki.eshop.api.model


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("condition")
    val condition: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("price")
    val price: Double,
    @SerializedName("productCode")
    val productCode: String,
    @SerializedName("productName")
    val productName: String,
)