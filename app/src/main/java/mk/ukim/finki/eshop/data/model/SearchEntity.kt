package mk.ukim.finki.eshop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import mk.ukim.finki.eshop.util.Constants.Companion.SEARCH_TABLE
import java.time.LocalDateTime

@Entity(tableName = SEARCH_TABLE)
data class SearchEntity(
    val text: String,
    val searchedOn: LocalDateTime
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}