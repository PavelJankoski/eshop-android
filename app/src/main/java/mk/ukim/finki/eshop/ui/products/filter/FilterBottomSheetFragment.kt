package mk.ukim.finki.eshop.ui.products.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.dto.PriceRangeDto
import mk.ukim.finki.eshop.databinding.FragmentFilterBottomSheetBinding
import mk.ukim.finki.eshop.databinding.FragmentProductsBinding
import mk.ukim.finki.eshop.ui.products.ProductsViewModel

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        binding.minValueTextView.text = binding.priceRangeSeekbar.currentMinValue.toString().plus(" MKD")
        binding.maxValueTextView.text = binding.priceRangeSeekbar.currentMaxValue.toString().plus(" MKD")

        binding.priceRangeSeekbar.setOnRangeSeekBarViewChangeListener(object : OnDoubleValueSeekBarChangeListener {
            override fun onStartTrackingTouch(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int
            ) {

            }

            override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {

            }

            override fun onValueChanged(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int,
                fromUser: Boolean
            ) {
                binding.minValueTextView.text = min.toString().plus(" MKD")
                binding.maxValueTextView.text = max.toString().plus(" MKD")
            }

        })

        binding.applyButton.setOnClickListener {
            val dto = PriceRangeDto(binding.priceRangeSeekbar.currentMinValue.toFloat(), binding.priceRangeSeekbar.currentMaxValue.toFloat())
            val action = FilterBottomSheetFragmentDirections.actionFilterBottomSheetFragmentToProductsFragment(1, "", dto)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}