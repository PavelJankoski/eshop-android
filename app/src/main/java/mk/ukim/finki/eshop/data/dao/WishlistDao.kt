package mk.ukim.finki.eshop.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.model.WishlistEntity

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistProduct(wishlistEntity: WishlistEntity)

    @Query("select COUNT(*) from wishlist where id=:id")
    suspend fun isProductInWishlist(id: Int): Int

    @Query("delete from wishlist where id=:id")
    suspend fun deleteProductFromWishlist(id: Int)

}