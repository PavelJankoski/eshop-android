package mk.ukim.finki.eshop.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.model.CategoriesEntity
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
    }
}