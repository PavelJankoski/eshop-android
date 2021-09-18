package mk.ukim.finki.eshop.api.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


data class UserProfilePicture(
    val base64format: String,
    val id: Int,
    val imageUrl: Any
) {
    fun imageBitmap(): Bitmap? {
        if (base64format.isEmpty()) {
            return null
        }
//        "data:%s;base64,%s"
        val base64 = base64format.split(";")[1].split(",")[1]
        val imageBytes = Base64.decode(base64, Base64.DEFAULT)
        val bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return bm
    }
}