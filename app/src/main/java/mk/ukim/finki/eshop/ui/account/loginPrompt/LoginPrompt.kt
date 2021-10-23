package mk.ukim.finki.eshop.ui.account.loginPrompt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentLoginPromptBinding

class LoginPrompt : BottomSheetDialogFragment() {

    private var _binding: FragmentLoginPromptBinding? = null
    private val binding get() = _binding!!

    val swipeEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPromptBinding.inflate(inflater, container, false)

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginPrompt_to_homeAccountFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}