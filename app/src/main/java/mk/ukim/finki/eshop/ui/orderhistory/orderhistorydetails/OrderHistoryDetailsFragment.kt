package mk.ukim.finki.eshop.ui.orderhistory.orderhistorydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.databinding.FragmentOrderHistoryDetailsBinding

@AndroidEntryPoint
class OrderHistoryDetailsFragment : Fragment() {
    private var _binding: FragmentOrderHistoryDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<OrderHistoryDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}