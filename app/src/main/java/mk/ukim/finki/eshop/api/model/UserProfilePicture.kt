package mk.ukim.finki.eshop.api.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64


data class UserProfilePicture(
    val base64format: String,
    val id: Int,
    val imageUrl: Any
) {
    fun imageBitmap(): Bitmap? {
        if (base64format.isEmpty()) {
            return null
        }
        val imageBytes = Base64.decode(base64format, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}