package mk.ukim.finki.eshop.ui.home.women

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentMenBinding
import mk.ukim.finki.eshop.databinding.FragmentWomenBinding


class WomenFragment : Fragment() {

    private var _binding: FragmentWomenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWomenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}