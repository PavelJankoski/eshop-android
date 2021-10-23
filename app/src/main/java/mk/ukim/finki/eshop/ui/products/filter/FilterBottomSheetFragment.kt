package mk.ukim.finki.eshop.ui.products.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import mk.ukim.finki.eshop.api.dto.request.FilterProductDto
import mk.ukim.finki.eshop.databinding.FragmentFilterBottomSheetBinding

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<FilterBottomSheetFragmentArgs>()


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
            val dto = FilterProductDto(args.categoryId, binding.priceRangeSeekbar.currentMinValue.toFloat(), binding.priceRangeSeekbar.currentMaxValue.toFloat())
            val action = FilterBottomSheetFragmentDirections.actionFilterBottomSheetFragmentToProductsFragment(1, dto, null, "")
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}