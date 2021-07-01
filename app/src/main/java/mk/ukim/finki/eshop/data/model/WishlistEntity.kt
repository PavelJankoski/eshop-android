package mk.ukim.finki.eshop.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue
import mk.ukim.finki.eshop.api.model.Image
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.util.Constants.Companion.WISHLIST_TABLE

@Entity(tableName = WISHLIST_TABLE)
data class WishlistEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val brand: String,
    val condition: String,
    val description: String,
    val rating: Float,
    val price: Double,
    val productCode: String,
    val name: String,
)