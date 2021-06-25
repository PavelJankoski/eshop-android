package mk.ukim.finki.eshop.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.model.CategoriesEntity
import mk.ukim.finki.eshop.ui.categories.CategoriesFragmentDirections
import mk.ukim.finki.eshop.util.NetworkResult

class CategoriesBinding {
    companion object{
        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<List<Category>>?,
            database: List<CategoriesEntity>?
        ) {
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
            when(view) {
                is TextView -> {
                    view.text = apiResponse?.message.toString()
                }
            }
        }

        @BindingAdapter("onCategoryClickListener")
        @JvmStatic
        fun onCategoryClickListener(categoryRowLayout: ConstraintLayout, category: Category) {
            categoryRowLayout.setOnClickListener {
                try {
                    val action = CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(category.id, category.type, null)
                    categoryRowLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.e("onCategoryClickListener", e.message.toString())
                }
            }
        }
    }
}