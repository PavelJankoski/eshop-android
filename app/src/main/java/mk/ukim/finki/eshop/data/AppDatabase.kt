package mk.ukim.finki.eshop.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mk.ukim.finki.eshop.data.dao.CategoriesDao
import mk.ukim.finki.eshop.data.dao.WishlistDao
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.data.typeconverters.CategoriesTypeConverter
import mk.ukim.finki.eshop.data.typeconverters.CommonConverters

@Database(
        entities = [CategoriesEntity::class, WishlistEntity::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(value = [CategoriesTypeConverter::class, CommonConverters::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun wishlistDao(): WishlistDao
}