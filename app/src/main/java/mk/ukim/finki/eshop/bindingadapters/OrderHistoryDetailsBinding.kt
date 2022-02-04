package mk.ukim.finki.eshop.bindingadapters

import android.widget.Button
import androidx.databinding.BindingAdapter

class OrderHistoryDetailsBinding {
    companion object {
        @BindingAdapter("reviewProductBtnText")
        @JvmStatic
        fun reviewProductBtnText(btn: Button, isReviewed: Boolean) {
            if (isReviewed) {
                btn.text = "Product reviewed!"
            } else {
                btn.text = "Review product"
            }
        }

    }
}