package mk.ukim.finki.eshop.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mk.ukim.finki.eshop.data.dao.CategoriesDao
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.typeconverters.CategoriesTypeConverter

@Database(
        entities = [CategoriesEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(CategoriesTypeConverter::class)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
}