package mk.ukim.finki.eshop.bindingadapters

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import mk.ukim.finki.eshop.api.model.Address
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.ui.categories.CategoriesFragmentDirections
import mk.ukim.finki.eshop.util.NetworkResult

class AddressBookBinding {
    companion object {
        @BindingAdapter("onAddressClickListener")
        @JvmStatic
        fun onAddressClickListener(addressRowLayout: ConstraintLayout, address: Address) {
            addressRowLayout.setOnClickListener {
                try {
//                    val action = CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(category.id, null, null, category.type)
//                    categoryRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
//                    Log.e("onCategoryClickListener", e.message.toString())
                }
            }
        }
        @BindingAdapter("isAddressDefault")
        @JvmStatic
        fun isAddressDefault(defaultLinearLayout: LinearLayout, isDefault: Boolean) {
            if(isDefault) {
                defaultLinearLayout.visibility = View.VISIBLE
            }
            else {
                defaultLinearLayout.visibility = View.GONE
            }
        }

    }
}