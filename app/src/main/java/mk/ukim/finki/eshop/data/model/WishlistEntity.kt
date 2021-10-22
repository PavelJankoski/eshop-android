package mk.ukim.finki.eshop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import mk.ukim.finki.eshop.util.Constants.Companion.WISHLIST_TABLE
import java.time.LocalDateTime

@Entity(tableName = WISHLIST_TABLE)
data class WishlistEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val addedOn: LocalDateTime,
    val brand: String,
    val condition: String,
    val description: String,
    val rating: Float,
    val price: Double,
    val productCode: String,
    val name: String,
    val images: String?
)