package mk.ukim.finki.eshop.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mk.ukim.finki.eshop.data.model.CategoriesEntity

@Dao
interface CategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categoriesEntity: CategoriesEntity)

    @Query("select * from categories")
    fun readCategories(): Flow<List<CategoriesEntity>>
}