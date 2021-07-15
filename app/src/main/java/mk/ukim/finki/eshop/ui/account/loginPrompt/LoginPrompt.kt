package mk.ukim.finki.eshop.ui.account.loginPrompt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentLoginPromptBinding

class LoginPrompt : BottomSheetDialogFragment() {

    private var _binding: FragmentLoginPromptBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPromptBinding.inflate(inflater, container, false)

        return binding.root
    }
}