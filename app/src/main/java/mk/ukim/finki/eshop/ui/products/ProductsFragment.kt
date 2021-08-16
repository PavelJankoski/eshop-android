package mk.ukim.finki.eshop.ui.products

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.ProductsGridAdapter
import mk.ukim.finki.eshop.adapters.ProductsListAdapter
import mk.ukim.finki.eshop.databinding.FragmentProductsBinding
import mk.ukim.finki.eshop.ui.products.filter.FilterBottomSheetFragmentDirections
import mk.ukim.finki.eshop.ui.search.SearchActivity
import mk.ukim.finki.eshop.util.Constants.Companion.SEARCH_HISTORY_EXTRAS
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import mk.ukim.finki.eshop.util.Utils.Companion.showShimmerEffect

@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private val args by navArgs<ProductsFragmentArgs>()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val productsViewModel by viewModels<ProductsViewModel>()
    private var menuItemType = true
    private val mAdapterList by lazy { ProductsListAdapter(productsViewModel) }
    private val mAdapterGrid by lazy { ProductsGridAdapter(productsViewModel) }
    private var searchedText: String = ""


    override fun onResume() {
        super.onResume()
        setupSortingDropdown()
        getProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        observeTypeMenuItemValue()
        binding.filterButton.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsFragmentToFilterBottomSheetFragment(args.categoryId)
            findNavController().navigate(action)
        }
        observeProductsResponse()
        observeAddOrRemoveToShoppingCartResponse()
        return binding.root
    }

    private fun observeAddOrRemoveToShoppingCartResponse() {
        productsViewModel.addOrRemoveProductResponse.observe(viewLifecycleOwner, { response ->
            if (response is NetworkResult.Success) {
                productsViewModel.addOrRemoveProductShoppingCart()
            } else if (response is NetworkResult.Error) {
                var errorMessage = ""
                if (response.message.equals("")) {
                    errorMessage = ""
                } else errorMessage = "Due to technical problems at the moment we can not execute you're action"
                Utils.showToast(requireContext(), errorMessage, Toast.LENGTH_SHORT)
            }
        })
    }


    private fun setupSortingDropdown() {
        val sortingCriteria = resources.getStringArray(R.array.sorting_products)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.sorting_dropdown_item, sortingCriteria)
        binding.sortingAutocomplete.setAdapter(arrayAdapter)
        binding.sortingAutocomplete.setOnItemClickListener { _, _, position, _ ->
            productsViewModel.orderProductsByCriteria(position)
        }
    }

    private fun getProducts() {
        if (searchedText.isNotEmpty()) {
            productsViewModel.getFilteredProductsForCategory(args.categoryId, searchedText)
        }
        else if(args.priceRangeDto != null) {
            productsViewModel.getProductsInPriceRange(args.priceRangeDto!!)
        }
        else {
            productsViewModel.getProductsForCategory(args.categoryId)
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val searchText = data?.getStringExtra(SEARCH_HISTORY_EXTRAS)
            if (searchText != null) {
                searchedText = searchText
            }
        }
    }

    private fun startSearchActivity() {
        val intent: Intent = Intent(activity, SearchActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun observeProductsResponse() {
        productsViewModel.productsResponse.observe(viewLifecycleOwner, {response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect(binding.productsShimmerFrameLayout, binding.productsRecyclerView)
                    if(!response.data.isNullOrEmpty()) {
                        mAdapterList.setData(response.data)
                        mAdapterGrid.setData(response.data)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect(binding.productsShimmerFrameLayout, binding.productsRecyclerView)
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect(binding.productsShimmerFrameLayout, binding.productsRecyclerView)
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
                searchedText = ""
                startSearchActivity()
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}