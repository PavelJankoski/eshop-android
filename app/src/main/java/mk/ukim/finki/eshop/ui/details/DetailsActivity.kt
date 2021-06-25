package mk.ukim.finki.eshop.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.CategoriesPagerAdapter
import mk.ukim.finki.eshop.adapters.DetailsPagerAdapter
import mk.ukim.finki.eshop.databinding.ActivityDetailsBinding
import mk.ukim.finki.eshop.databinding.ActivityMainBinding
import mk.ukim.finki.eshop.ui.categories.man.CategoriesManFragment
import mk.ukim.finki.eshop.ui.categories.woman.CategoriesWomanFragment
import mk.ukim.finki.eshop.ui.details.moredetails.MoreDetailsFragment
import mk.ukim.finki.eshop.ui.details.reviews.ReviewsFragment
import mk.ukim.finki.eshop.ui.products.ProductsFragmentArgs
import mk.ukim.finki.eshop.util.Constants.Companion.PRODUCT_RESULT_KEY

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        binding.product = args.product
        binding.lifecycleOwner = this
        setContentView(binding.root)
        binding.backFab.setOnClickListener {
            finish()
        }
        args.product.images?.map { SlideModel(it.imageUrl, ScaleTypes.CENTER_CROP) }?.let {
            binding.imageSlider.setImageList(
                it
            )
        }
        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(MoreDetailsFragment(), ReviewsFragment())
        val tabLayoutTitles = arrayListOf<String>("More details", "Reviews")
        val resultBundle = Bundle()
        resultBundle.putParcelable(PRODUCT_RESULT_KEY, args.product)
        val pagerAdapter = DetailsPagerAdapter(resultBundle, fragments, this)
        binding.detailsViewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.detailsTabLayout, binding.detailsViewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }
}