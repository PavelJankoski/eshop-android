package mk.ukim.finki.eshop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.util.Constants.Companion.CATEGORY_TABLE

@Entity(tableName = CATEGORY_TABLE)
class CategoriesEntity(
    var categories: List<Category>
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}