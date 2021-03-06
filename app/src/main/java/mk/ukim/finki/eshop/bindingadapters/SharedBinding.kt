package mk.ukim.finki.eshop.bindingadapters

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
                placeholder(R.drawable.ic_placeholder_image)
            }
        }

        @BindingAdapter("onlyFirstLetterCapital")
        @JvmStatic
        fun onlyFirstLetterCapital(tv: TextView, word: String) {
            tv.text = String.format(
                "%s%s",
                word.substring(0, 1).uppercase(),
                word.substring(1).lowercase()
            )
        }

        @BindingAdapter("errorMessage")
        @JvmStatic
        fun setErrorMessage(textView: TextInputLayout, message: String?) {
            textView.error = message
        }

        @BindingAdapter("setupOutlinedButtonStyle", "isLoggedIn", requireAll = false)
        @JvmStatic
        fun setupOutlinedButtonStyle(btn: MaterialButton, product: Product, isLoggedIn: Boolean) {
            if (!isLoggedIn) {
                btn.text = "Please log in"
            } else if (product.sizes.isEmpty()) {
                btn.text = "Out of stock"
            } else {
                btn.text = "Please select size"
            }
            btn.isEnabled = false
            btn.setTextColor(ContextCompat.getColor(btn.context, R.color.lightMediumGray))
            btn.strokeColor =
                ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.lightMediumGray))
        }

        @BindingAdapter("isDisabledFab")
        @JvmStatic
        fun isDisabledFab(btn: FloatingActionButton, isLoggedIn: Boolean) {
            if (!isLoggedIn) {
                btn.isEnabled = false
                btn.setColorFilter(ContextCompat.getColor(btn.context, R.color.lightMediumGray))
            } else {
                btn.isEnabled = true
                btn.setColorFilter(ContextCompat.getColor(btn.context, R.color.black))
            }
        }

        @BindingAdapter("safeTextOrDash")
        @JvmStatic
        fun safeTextOrDash(
            textView: TextView, text: String?
        ) {
            if (text.isNullOrBlank()) {
                textView.text = "-"
            } else {
                textView.text = text
            }
        }

//        @BindingAdapter("formatDateFromString")
//        @JvmStatic
//        fun formatDateFromString(tv: TextView, date: String) {
//            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//            val output = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
//            val d = sdf.parse(date)
//            val formattedTime = output.format(d!!)
//            tv.text = formattedTime
//        }

    }

    fun setupButtonMoveToBag(btn: MaterialButton) {
        btn.text = "Please select size"
        // btn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.green))
        btn.strokeColor =
            ColorStateList.valueOf(ContextCompat.getColor(btn.context, R.color.lightMediumGray))
    }
}