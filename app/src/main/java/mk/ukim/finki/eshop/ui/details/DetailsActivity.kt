package mk.ukim.finki.eshop.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.DetailsPagerAdapter
import mk.ukim.finki.eshop.databinding.ActivityDetailsBinding
import mk.ukim.finki.eshop.ui.details.moredetails.MoreDetailsFragment
import mk.ukim.finki.eshop.ui.details.reviews.ReviewsFragment
import mk.ukim.finki.eshop.util.Constants.Companion.PRODUCT_RESULT_KEY
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    private val detailsViewModel by viewModels<DetailsViewModel>()

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
        setupAddToWishlistBtn()
        setupAddToBagBtn()
        setupViewPager()
    }

    private fun setupAddToWishlistBtn() {
        binding.addToWishlistFab.setColorFilter(ContextCompat.getColor(binding.addToWishlistFab.context, R.color.black))
        if(args.product.isFavourite) {
            binding.addToWishlistFab.setImageResource(R.drawable.ic_heart_full)
        }
        else {
            binding.addToWishlistFab.setImageResource(R.drawable.ic_heart)
        }
        binding.addToWishlistFab.setOnClickListener {
            if(args.product.isFavourite) {
                binding.addToWishlistFab.setImageResource(R.drawable.ic_heart)
                args.product.isFavourite = false
                detailsViewModel.deleteProductFromWishlist(args.product.id)
                Utils.showSnackbar(binding.addToWishlistFab, "Removed product from wishlist!", Snackbar.LENGTH_SHORT)
            }
            else {
                binding.addToWishlistFab.setImageResource(R.drawable.ic_heart_full)
                args.product.isFavourite = true
                detailsViewModel.insertProductInWishlist(args.product)
                Utils.showSnackbar(binding.addToWishlistFab, "Added product to wishlist!", Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun setupAddToBagBtn() {
        if(args.product.isInShoppingCart) {
            binding.addToBagBtn.text = getString(R.string.remove_from_bag)
        }
        else {
            binding.addToBagBtn.text = getString(R.string.add_to_bag)
        }
        binding.addToBagBtn.setOnClickListener {
            if(args.product.isInShoppingCart) {
                binding.addToBagBtn.text = getString(R.string.add_to_bag)
                detailsViewModel.removeProductFromShoppingCart(args.product.id)
                Utils.showSnackbar(binding.addToBagBtn, "Removed product from shopping bag!", Snackbar.LENGTH_SHORT)
            }
            else {
                binding.addToBagBtn.text = getString(R.string.remove_from_bag)
                detailsViewModel.addProductToShoppingCart(args.product.id)
                Utils.showSnackbar(binding.addToBagBtn, "Added product to shopping bag!", Snackbar.LENGTH_SHORT)
            }
        }
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