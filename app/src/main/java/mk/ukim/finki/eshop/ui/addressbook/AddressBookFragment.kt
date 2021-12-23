package mk.ukim.finki.eshop.ui.addressbook

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.AddressBookAdapter
import mk.ukim.finki.eshop.adapters.CategoriesAdapter
import mk.ukim.finki.eshop.databinding.FragmentAddressBookBinding
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding
import mk.ukim.finki.eshop.ui.products.ProductsViewModel
import mk.ukim.finki.eshop.util.GlobalVariables
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class AddressBookFragment : Fragment() {
    private var _binding: FragmentAddressBookBinding? = null
    private val binding get() = _binding!!
    private val addressBookViewModel by viewModels<AddressBookViewModel>()
    private val mAdapter by lazy { AddressBookAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBookBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupRecyclerView()
        observeAddressesResponse()
        addressBookViewModel.getAddressesForUser()
        return binding.root
    }

    private fun observeAddressesResponse() {
        addressBookViewModel.addressesResponse.observe(viewLifecycleOwner, {response ->
            when(response) {
                is NetworkResult.Success -> {
                    Utils.hideShimmerEffect(
                        binding.addressBookShimmerFrameLayout,
                        binding.addressBookRecyclerView
                    )
                    if(response.data != null) {
                        mAdapter.setData(response.data)
                    }
                }
                is NetworkResult.Error -> {
                    Utils.hideShimmerEffect(
                        binding.addressBookShimmerFrameLayout,
                        binding.addressBookRecyclerView
                    )
                }
                is NetworkResult.Loading -> {
                    Utils.showShimmerEffect(
                        binding.addressBookShimmerFrameLayout,
                        binding.addressBookRecyclerView
                    )
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_menuItem -> {
                findNavController().navigate(AddressBookFragmentDirections.actionAddressBookFragmentToEnterAddressFragment())
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        binding.addressBookRecyclerView.adapter = mAdapter
        binding.addressBookRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}