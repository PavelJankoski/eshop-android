package mk.ukim.finki.eshop.bindingadapters

import android.view.View
import android.widget.LinearLayout
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
    }
}