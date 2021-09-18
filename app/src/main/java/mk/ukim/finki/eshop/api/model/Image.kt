package mk.ukim.finki.eshop.api.model


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.util.Base64
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
data class Image(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("base64format")
    val base64format: String?,
) : Parcelable {
    fun imageBitmap(): Bitmap? {
        if (base64format.isNullOrEmpty()) {
            return null
        }
//        "data:%s;base64,%s"
        val base64 = base64format.split(";")[1].split(",")[1]
        val imageBytes = Base64.decode(base64, Base64.DEFAULT)
        val bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return bm
    }
}