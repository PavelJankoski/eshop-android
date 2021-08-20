package mk.ukim.finki.eshop.bindingadapters

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.Product

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
        fun setErrorMessage(textView: TextInputLayout, message: String?) {
            textView.error = message
        }

        @BindingAdapter("setupOutlinedButtonStyle")
        @JvmStatic
        fun setupOutlinedButtonStyle(btn: MaterialButton, product: Product) {
            val sb = SharedBinding()
            if(!product.isInShoppingCart) {
                sb.setupButtonMoveToBag(btn)
            }
            else {
                sb.setupButtonRemove(btn)
            }
        }
    }
    fun setupButtonMoveToBag(btn: MaterialButton) {
        btn.text = "Move to bag"
        btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.green))
    }
    fun setupButtonRemove(btn: MaterialButton) {
        btn.text = "Remove from bag"
        btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.red))
    }
}