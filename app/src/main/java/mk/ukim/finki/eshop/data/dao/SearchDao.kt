package mk.ukim.finki.eshop.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mk.ukim.finki.eshop.data.model.SearchEntity
import mk.ukim.finki.eshop.data.model.WishlistEntity

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchText(searchEntity: SearchEntity)

    @Query("select * from search_history")
    fun readSearchHistory(): Flow<List<SearchEntity>>

    @Query("delete from search_history where id=:id")
    suspend fun deleteSearch(id: Int)

    @Query("delete from search_history")
    suspend fun deleteAllSearchHistory()
}