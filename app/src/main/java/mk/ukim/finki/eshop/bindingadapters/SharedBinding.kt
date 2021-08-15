package mk.ukim.finki.eshop.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import mk.ukim.finki.eshop.R

class SharedBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_placeholder_image)
            }
        }

        @BindingAdapter("onlyFirstLetterCapital")
        @JvmStatic
        fun onlyFirstLetterCapital(tv: TextView, word: String) {
            tv.text = String.format("%s%s", word.substring(0, 1).uppercase(), word.substring(1).lowercase())
        }

        @BindingAdapter("errorMessage")
        @JvmStatic
        fun setErrorMessage(textView: TextView, message: String?) {
            textView.error = message
        }
    }
}