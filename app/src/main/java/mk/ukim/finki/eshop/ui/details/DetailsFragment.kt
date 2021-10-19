package mk.ukim.finki.eshop.ui.details

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.DetailsPagerAdapter
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding
import mk.ukim.finki.eshop.databinding.FragmentDetailsBinding
import mk.ukim.finki.eshop.ui.details.moredetails.MoreDetailsFragment
import mk.ukim.finki.eshop.ui.details.reviews.ReviewsFragment
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.GlobalVariables
import mk.ukim.finki.eshop.util.Utils

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
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.product = args.product
        binding.isLoggedIn = detailsViewModel.loginManager.readToken() != ""
        binding.lifecycleOwner = this
        args.product.images?.map { SlideModel(it.imageUrl, ScaleTypes.CENTER_CROP) }?.let {
            binding.imageSlider.setImageList(
                it
            )
        }
        binding.backFab.setOnClickListener {
            findNavController().popBackStack()
        }
        Utils.setupCartItemsBadge(binding.cartBadge, GlobalVariables.productsInBagNumber.value!!)
        observeShoppingBagActions()
        observeWishlistActions()
        setupAddToWishlistBtn()
        setupAddToBagBtn()
        setupViewPager()
        return binding.root
    }

    private fun observeShoppingBagActions() {
        detailsViewModel.addProductToBagResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                addProductToBagSuccess()
            }
        })
        detailsViewModel.removeProductFromBagResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                removeProductFromBagSuccess()
            }
        })
    }

    private fun observeWishlistActions() {
        detailsViewModel.addProductToWishlistResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                addProductToWishlistSuccess()
            }
        })
        detailsViewModel.removeProductFromWishlistResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                removeProductFromWishlistSuccess()
            }
        })
    }

    private fun addProductToBagSuccess() {
        Utils.setupCartItemsBadge(binding.cartBadge, GlobalVariables.productsInBagNumber.value!!)
        args.product.isInShoppingCart = true
        binding.addToBagBtn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(binding.addToBagBtn.context, R.color.red))
        binding.addToBagBtn.text = getString(R.string.remove_from_bag)
        Utils.showSnackbar(binding.addToBagBtn, "Added product to shopping bag!", Snackbar.LENGTH_SHORT)
    }

    private fun removeProductFromBagSuccess() {
        Utils.setupCartItemsBadge(binding.cartBadge, GlobalVariables.productsInBagNumber.value!!)
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
                detailsViewModel.removeProductFromShoppingCart(args.product.id, args.product.price.toInt())
            }
            else {
                detailsViewModel.addProductToShoppingCart(args.product.id, args.product.price.toInt())
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}