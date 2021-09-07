package mk.ukim.finki.eshop.ui.details.moredetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.databinding.FragmentMoreDetailsBinding
import mk.ukim.finki.eshop.databinding.FragmentWishlistBinding
import mk.ukim.finki.eshop.util.Constants.Companion.PRODUCT_RESULT_KEY


class MoreDetailsFragment : Fragment() {

    private var _binding: FragmentMoreDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoreDetailsBinding.inflate(inflater, container, false)
        val myBundle: Product = requireArguments().getParcelable(PRODUCT_RESULT_KEY)!!
        binding.product = myBundle
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}