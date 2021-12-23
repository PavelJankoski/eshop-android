package mk.ukim.finki.eshop.ui.addressbook.enteraddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.FragmentAddressBookBinding
import mk.ukim.finki.eshop.databinding.FragmentEnterAddressBinding
import mk.ukim.finki.eshop.ui.products.ProductsFragmentArgs
import mk.ukim.finki.eshop.ui.userinfo.UserInfoFormValidator


class EnterAddressFragment : Fragment() {
    private var _binding: FragmentEnterAddressBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EnterAddressFragmentArgs>()
    private val validator = EnterAddressFormValidator()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterAddressBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.validator = validator
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}