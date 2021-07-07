package mk.ukim.finki.eshop.bindingadapters

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
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
                val intent = Intent()
                intent.putExtra(Constants.SEARCH_HISTORY_EXTRAS, text)
                (constraintLayout.context as Activity).setResult(Activity.RESULT_OK, intent)
                (constraintLayout.context as Activity).finish()
            }
        }
    }
}