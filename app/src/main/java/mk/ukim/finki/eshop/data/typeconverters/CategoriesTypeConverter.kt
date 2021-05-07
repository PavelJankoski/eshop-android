package mk.ukim.finki.eshop.data.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mk.ukim.finki.eshop.api.model.Category

class CategoriesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun categoriesToString(categories: List<Category>): String {
        return gson.toJson(categories)
    }

    @TypeConverter
    fun stringToCategories(data: String): List<Category> {
        val listType = object: TypeToken<List<Category>>() {}.type
        return gson.fromJson(data, listType)
    }
}