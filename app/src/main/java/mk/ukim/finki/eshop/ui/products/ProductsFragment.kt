package mk.ukim.finki.eshop.ui.products

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.ProductsGridAdapter
import mk.ukim.finki.eshop.adapters.ProductsListAdapter
import mk.ukim.finki.eshop.databinding.FragmentProductsBinding
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils

@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private val args by navArgs<ProductsFragmentArgs>()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val productsViewModel by viewModels<ProductsViewModel>()
    private var menuItemType = true
    private val mAdapterList by lazy { ProductsListAdapter() }
    private val mAdapterGrid by lazy { ProductsGridAdapter() }


    override fun onResume() {
        super.onResume()
        setupSortingDropdown()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        getAndObserveProductsResponse()
        observeTypeMenuItemValue()
        return binding.root
    }



    private fun setupSortingDropdown() {
        val sortingCriteria = resources.getStringArray(R.array.sorting_products)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.sorting_dropdown_item, sortingCriteria)
        binding.sortingAutocomplete.setAdapter(arrayAdapter)
        binding.sortingAutocomplete.setOnItemClickListener { _, _, position, _ ->
            productsViewModel.orderProductsByCriteria(position)
        }
    }

    private fun getAndObserveProductsResponse() {
        productsViewModel.getProductsForCategory(args.categoryId)
        productsViewModel.productsResponse.observe(viewLifecycleOwner, {response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    if(!response.data.isNullOrEmpty()) {
                        mAdapterList.setData(response.data)
                        mAdapterGrid.setData(response.data)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })

    }



    private fun observeTypeMenuItemValue() {
        productsViewModel.listingType.observe(viewLifecycleOwner, { type ->
            if (type) {
                binding.productsRecyclerView.adapter = mAdapterList
                binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            } else {
                binding.productsRecyclerView.adapter = mAdapterGrid
                binding.productsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_toolbar_menu, menu)
        val menuItem = menu.findItem(R.id.shoppingCart_menuItem)
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoppingCart_menuItem -> {
                Utils.showToast(requireContext(), "Bag clicked!", Toast.LENGTH_SHORT)
                true
            }
            R.id.search_menuItem -> {
                Utils.showToast(requireContext(), "Search clicked!", Toast.LENGTH_SHORT)
                true
            }
            R.id.type_menuItem -> {
                menuItemType = !menuItemType
                if(menuItemType) {
                    item.setIcon(R.drawable.ic_list)
                }
                else {
                    item.setIcon(R.drawable.ic_grid)
                }
                productsViewModel.changeListing()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showShimmerEffect() {
        binding.productsshimmerFrameLayout.startShimmer()
        binding.productsshimmerFrameLayout.visibility = View.VISIBLE
        binding.productsRecyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        if(binding.productsshimmerFrameLayout.isShimmerVisible) {
            binding.productsshimmerFrameLayout.visibility = View.GONE
            binding.productsshimmerFrameLayout.stopShimmer()
        }
        binding.productsRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}