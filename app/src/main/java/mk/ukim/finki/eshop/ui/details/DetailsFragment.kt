package mk.ukim.finki.eshop.ui.details

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.DetailsPagerAdapter
import mk.ukim.finki.eshop.api.model.Size
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding
import mk.ukim.finki.eshop.databinding.FragmentDetailsBinding
import mk.ukim.finki.eshop.ui.details.moredetails.MoreDetailsFragment
import mk.ukim.finki.eshop.ui.details.reviews.ReviewsFragment
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.GlobalVariables
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

import com.google.android.material.chip.ChipDrawable




@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel by viewModels<DetailsViewModel>()
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.product = args.product
        binding.isLoggedIn = detailsViewModel.loginManager.readToken() != ""
        binding.lifecycleOwner = this
        args.product.images.map { SlideModel(it, ScaleTypes.CENTER_CROP) }.let {
            binding.imageSlider.setImageList(
                it
            )
        }
        binding.backFab.setOnClickListener {
            findNavController().popBackStack()
        }
        Utils.setupCartItemsBadge(binding.cartBadge, GlobalVariables.productsInBagNumber.value!!)
        observeShoppingBagActions()
        observeAddOrRemoveProductFromWishlist()
        setupAddToWishlistBtn()
        setupAddToBagBtn()
        setupViewPager()
        setupChipGroup()
        return binding.root
    }

    private fun observeShoppingBagActions() {
        detailsViewModel.addProductToBagResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                addProductToBagSuccess()
            }
        })
    }

    private fun observeAddOrRemoveProductFromWishlist() {
        detailsViewModel.removeProductFromWishlistResponse.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkResult.Success -> {
                    removeProductFromWishlistSuccess()
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(binding.root, "Error removing product from wishlist!", Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        })
        detailsViewModel.addProductToWishlistResponse.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkResult.Success -> {
                   addProductToWishlistSuccess()
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(binding.root, "Error adding product to wishlist!", Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        })
    }


    private fun addProductToBagSuccess() {
        Utils.setupCartItemsBadge(binding.cartBadge, GlobalVariables.productsInBagNumber.value!!)
        Utils.showSnackbar(binding.addToBagBtn, "Added product to shopping bag!", Snackbar.LENGTH_SHORT)
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
                detailsViewModel.removeProductFromWishlistForUser(args.product.id)
            }
            else {
                detailsViewModel.addProductToWishlistForUser(args.product.id)
            }
        }
    }

    private fun setupAddToBagBtn() {
        binding.addToBagBtn.setOnClickListener {
            detailsViewModel.addProductToShoppingCart(args.product.id, args.product.price.toInt())
        }
    }



    private fun setupViewPager() {
        val fragments = arrayListOf<Fragment>(MoreDetailsFragment(), ReviewsFragment())
        val tabLayoutTitles = arrayListOf<String>("More details", "Reviews")
        val resultBundle = Bundle()
        resultBundle.putParcelable(Constants.PRODUCT_RESULT_KEY, args.product)
        val pagerAdapter = DetailsPagerAdapter(resultBundle, fragments, requireActivity())
        binding.detailsViewPager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.detailsTabLayout, binding.detailsViewPager) { tab, position ->
            tab.text = tabLayoutTitles[position]
        }.attach()
    }

    private fun setupChipGroup() {
        if(args.product.sizes.isNotEmpty()) {
            binding.chipGroupScrollView.visibility = View.VISIBLE
            binding.outOfStockTextView.visibility = View.GONE
            args.product.sizes.forEach {
                val chip = layoutInflater.inflate(R.layout.custom_size_chip_layout, binding.sizeChipGroup, false) as Chip
                chip.text = it.name.uppercase()
                chip.id = ViewCompat.generateViewId()
                chip.isCheckedIconVisible = true
                chip.isCheckable = true
                chip.isClickable = true
                chip.tag = it.id
                binding.sizeChipGroup.addView(chip)
            }
        }
        else {
            binding.chipGroupScrollView.visibility = View.GONE
            binding.outOfStockTextView.visibility = View.VISIBLE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}