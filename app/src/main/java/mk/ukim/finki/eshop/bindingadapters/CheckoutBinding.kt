package mk.ukim.finki.eshop.bindingadapters

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import mk.ukim.finki.eshop.api.model.Address

class CheckoutBinding {
    companion object {
        @BindingAdapter("handleEmptyAddress")
        @JvmStatic
        fun handleEmptyAddress(
            linearLayout: LinearLayout, address: Address?
        ) {
            if (address != null) {
                linearLayout.visibility = View.VISIBLE
            } else {
                linearLayout.visibility = View.GONE
            }
        }

        @BindingAdapter("handleEmptyAddressTextView")
        @JvmStatic
        fun handleEmptyAddressTextView(
            textView: TextView, address: Address?
        ) {
            if (address != null) {
                textView.visibility = View.GONE
            } else {
                textView.visibility = View.VISIBLE
            }
        }
    }
}