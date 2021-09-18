package mk.ukim.finki.eshop.ui.details.reviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.ProductsListAdapter
import mk.ukim.finki.eshop.adapters.ReviewsAdapter
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.databinding.FragmentReviewsBinding
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.util.Constants


class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { ReviewsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        val myBundle: Product = requireArguments().getParcelable(Constants.PRODUCT_RESULT_KEY)!!
        binding.reviewsRecyclerView.adapter = mAdapter
        binding.reviewsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if(!myBundle.ratings.isNullOrEmpty()) {
            mAdapter.setData(myBundle.ratings)
            binding.root.requestLayout()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}