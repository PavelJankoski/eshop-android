package mk.ukim.finki.eshop.data.typeconverters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class CommonConverters {
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return if (dateString == null) {
            null
        } else {
            LocalDateTime.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}