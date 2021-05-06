package mk.ukim.finki.eshop.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import mk.ukim.finki.eshop.R

class SharedBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_placeholder_image)
            }
        }
    }
}