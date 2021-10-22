package mk.ukim.finki.eshop.bindingadapters

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import mk.ukim.finki.eshop.ui.search.SearchFragmentDirections
import mk.ukim.finki.eshop.ui.search.SearchViewModel
import mk.ukim.finki.eshop.util.Constants

class SearchBinding {
    companion object {

        @JvmStatic
        @BindingAdapter("deleteSearchText", "setViewModel", requireAll = true)
        fun deleteSearchText(iv: ImageView, id: Int, vm: SearchViewModel) {
            iv.setOnClickListener {
                vm.deleteSearchText(id)
            }
        }

        @JvmStatic
        @BindingAdapter("searchRowClickListener")
        fun searchRowClickListener(constraintLayout: ConstraintLayout, text: String) {
            constraintLayout.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToProductsFragment(1, null, text, "\"".plus(text).plus("\""))
                constraintLayout.findNavController().navigate(action)
            }
        }
    }
}