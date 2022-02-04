package mk.ukim.finki.eshop.ui.orderhistory.reviewproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.databinding.FragmentReviewProductBinding
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class ReviewProductFragment : Fragment() {
    private var _binding: FragmentReviewProductBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ReviewProductFragmentArgs>()
    private val reviewProductViewModel by viewModels<ReviewProductViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewProductBinding.inflate(inflater, container, false)
        handleLeaveReviewButtonClick()
        observeLeaveRatingResponse()
        return binding.root
    }

    private fun observeLeaveRatingResponse() {
        reviewProductViewModel.leaveReviewResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Utils.showSnackbar(
                        binding.root,
                        "Thank you for your feedback!",
                        Snackbar.LENGTH_SHORT
                    )
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {
                    Utils.showToast(
                        requireContext(),
                        "Error leaving review for this product",
                        Toast.LENGTH_SHORT
                    )
                }
                else -> {}
            }
        }
    }

    private fun handleLeaveReviewButtonClick() {
        binding.reviewButton.setOnClickListener {
            if (binding.ratingBar.rating != 0.0f) {
                reviewProductViewModel.leaveReview(
                    args.productId,
                    binding.reviewEditText.text.toString(),
                    binding.ratingBar.rating
                )
            } else {
                Utils.showToast(requireContext(), "Please select some rating", Toast.LENGTH_SHORT)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}