package mk.ukim.finki.eshop.ui.qrcode

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.databinding.FragmentQrCodeBinding
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class QrCodeFragment : Fragment() {
    private var _binding: FragmentQrCodeBinding? = null
    private val binding get() = _binding!!
    private val qrCodeViewModel by viewModels<QrCodeViewModel>()
    private lateinit var codeScanner: CodeScanner

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                qrCodeViewModel.getProductByCode(it.text)
            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        observeProduct()
        return binding.root
    }

    private fun observeProduct() {
            qrCodeViewModel.product.observe(viewLifecycleOwner, {response ->
                when(response) {
                    is NetworkResult.Success -> {
                        val action = QrCodeFragmentDirections.actionQrCodeFragmentToDetailsFragment(response.data!!)
                        findNavController().navigate(action)
                    }
                    is NetworkResult.Error -> {
                       Utils.showToast(requireContext(), response.message!!, Toast.LENGTH_LONG)
                    }
                }
            })
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}