package mk.ukim.finki.eshop.ui.details

import android.content.res.ColorStateList
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
import mk.ukim.finki.eshop.util.GlobalVariables.Companion.productsInBagNumber
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
        binding.isLoggedIn = detailsViewModel.loginManager.readToken() != ""
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
        Utils.setupCartItemsBadge(binding.cartBadge, productsInBagNumber.value!!)
        observeShoppingBagActions()
        observeWishlistActions()
        setupAddToWishlistBtn()
        setupAddToBagBtn()
        setupViewPager()
    }

    private fun observeShoppingBagActions() {
        detailsViewModel.addProductToBagResponse.observe(this, {
            if(it.data!!) {
                addProductToBagSuccess()
            }
        })
        detailsViewModel.removeProductFromBagResponse.observe(this, {
            if(it.data!!) {
                removeProductFromBagSuccess()
            }
        })
    }

    private fun observeWishlistActions() {
        detailsViewModel.addProductToWishlistResponse.observe(this, {
            if(it.data!!) {
                addProductToWishlistSuccess()
            }
        })
        detailsViewModel.removeProductFromWishlistResponse.observe(this, {
            if(it.data!!) {
                removeProductFromWishlistSuccess()
            }
        })
    }

    private fun addProductToBagSuccess() {
        Utils.setupCartItemsBadge(binding.cartBadge, productsInBagNumber.value!!)
        args.product.isInShoppingCart = true
        binding.addToBagBtn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(binding.addToBagBtn.context, R.color.red))
        binding.addToBagBtn.text = getString(R.string.remove_from_bag)
        Utils.showSnackbar(binding.addToBagBtn, "Added product to shopping bag!", Snackbar.LENGTH_SHORT)
    }

    private fun removeProductFromBagSuccess() {
        Utils.setupCartItemsBadge(binding.cartBadge, productsInBagNumber.value!!)
        args.product.isInShoppingCart = false
        binding.addToBagBtn.text = getString(R.string.add_to_bag)
        binding.addToBagBtn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(binding.addToBagBtn.context, R.color.green))
        Utils.showSnackbar(binding.addToBagBtn, "Removed product from shopping bag!", Snackbar.LENGTH_SHORT)
    }

    private fun addProductToWishlistSuccess() {
        binding.addToWishlistFab.setImageResource(R.drawable.ic_heart_full)
        args.product.isFavourite = true
        Utils.showSnackbar(binding.addToWishlistFab, "Added product to wishlist!", Snackbar.LENGTH_SHORT)
    }

    private fun removeProductFromWishlistSuccess() {
        binding.addToWishlistFab.setImageResource(R.drawable.ic_heart)
        args.product.isFavourite = false
        Utils.showSnackbar(binding.addToWishlistFab, "Removed product from wishlist!", Snackbar.LENGTH_SHORT)
    }

    private fun setupAddToWishlistBtn() {
        if(args.product.isFavourite) {
            binding.addToWishlistFab.setImageResource(R.drawable.ic_heart_full)
        }
        else {
            binding.addToWishlistFab.setImageResource(R.drawable.ic_heart)
        }
        binding.addToWishlistFab.setOnClickListener {
            if(args.product.isFavourite) {
                detailsViewModel.deleteProductFromWishlist(args.product.id)
            }
            else {
                detailsViewModel.insertProductInWishlist(args.product)
            }
        }
    }

    private fun setupAddToBagBtn() {
        binding.addToBagBtn.setOnClickListener {
            if(args.product.isInShoppingCart) {
                detailsViewModel.removeProductFromShoppingCart(args.product.id)
            }
            else {
                detailsViewModel.addProductToShoppingCart(args.product.id)
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