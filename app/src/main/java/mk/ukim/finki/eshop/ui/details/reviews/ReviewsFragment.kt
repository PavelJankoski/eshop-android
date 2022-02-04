package mk.ukim.finki.eshop.ui.details.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.ReviewsAdapter
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.databinding.FragmentReviewsBinding
import mk.ukim.finki.eshop.util.Constants
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { ReviewsAdapter() }
    private val reviewsViewModel by viewModels<ReviewsViewModel>()
    private lateinit var myBundle: Product

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        myBundle = requireArguments().getParcelable(Constants.PRODUCT_RESULT_KEY)!!
        binding.reviewsRecyclerView.adapter = mAdapter
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeReviewsResponse()
        reviewsViewModel.getReviewsForProduct(myBundle.id)
        handleOnChipChange()
        return binding.root
    }

    private fun observeReviewsResponse() {
        reviewsViewModel.reviewsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(
                        binding.reviewsShimmerFrameLayout,
                        binding.reviewsRecyclerView
                    )
                    if (!response.data.isNullOrEmpty()) {
                        reviewsNotEmpty()
                        mAdapter.setData(response.data)
                    } else {
                        reviewsEmpty()
                    }
                }
                is NetworkResult.Error -> {
                    Utils.hideShimmerEffect(
                        binding.reviewsShimmerFrameLayout,
                        binding.reviewsRecyclerView
                    )
                    reviewsEmpty()
                    binding.ratingChipGroupScrollView.visibility = View.INVISIBLE
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(
                        binding.reviewsShimmerFrameLayout,
                        binding.reviewsRecyclerView
                    )
                    binding.reviewsLottie.visibility = View.GONE
                }
            }
        }
    }

    private fun handleOnChipChange() {
        binding.ratingChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedRatingChipText = chip.text.toString().lowercase()
            if(selectedRatingChipText.equals("all")) {
                reviewsViewModel.getReviewsForProduct(myBundle.id)
            }
            else {
                reviewsViewModel.getReviewsByRatingForProduct(myBundle.id, selectedRatingChipText.toFloat())
            }
        }
    }

    private fun reviewsNotEmpty() {
        binding.ratingChipGroupScrollView.visibility = View.VISIBLE
        binding.reviewsRecyclerView.visibility = View.VISIBLE
        binding.reviewsLottie.visibility = View.GONE
    }

    private fun reviewsEmpty() {
        binding.reviewsRecyclerView.visibility = View.GONE
        binding.reviewsLottie.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}