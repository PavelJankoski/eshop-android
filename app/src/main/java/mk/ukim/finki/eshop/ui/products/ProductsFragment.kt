package mk.ukim.finki.eshop.ui.products

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.ProductsGridAdapter
import mk.ukim.finki.eshop.adapters.ProductsListAdapter
import mk.ukim.finki.eshop.databinding.FragmentProductsBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.Constants.Companion.SEARCH_HISTORY_EXTRAS
import mk.ukim.finki.eshop.util.GlobalVariables.Companion.productsInBagNumber
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import mk.ukim.finki.eshop.util.Utils.Companion.showShimmerEffect
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    @Inject lateinit var loginManager: LoginManager

    private val args by navArgs<ProductsFragmentArgs>()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val productsViewModel by viewModels<ProductsViewModel>()
    private var menuItemType = true
    private val mAdapterList by lazy { ProductsListAdapter(productsViewModel, productsViewModel.loginManager.loggedIn.value) }
    private val mAdapterGrid by lazy { ProductsGridAdapter(productsViewModel, productsViewModel.loginManager.loggedIn.value) }
    private lateinit var menuItem: MenuItem


    override fun onResume() {
        super.onResume()
        setupSortingDropdown()
        if(this::menuItem.isInitialized) {
            val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
            Utils.setupCartItemsBadge(tv, productsInBagNumber.value!!)
        }
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
        setupSwipeToRefresh()
        return binding.root
    }

    private fun setupSwipeToRefresh () {
        binding.productsSwipeRefresh.setOnRefreshListener {
            getProducts()
        }
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
        if (!args.searchText.isNullOrEmpty()) {
            productsViewModel.getFilteredProductsForCategory(args.categoryId, args.searchText!!)
        }
        else if(args.priceRangeDto != null) {
            productsViewModel.getProductsInPriceRange(args.priceRangeDto!!)
        }
        else {
            productsViewModel.getProductsForCategory(args.categoryId)
        }
    }


    private fun navigateToSearchFragment() {
        val action = ProductsFragmentDirections.actionProductsFragmentToSearchFragment()
        findNavController().navigate(action)
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
                    binding.productsSwipeRefresh.isRefreshing = false
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect(binding.productsShimmerFrameLayout, binding.productsRecyclerView)
                    binding.productsSwipeRefresh.isRefreshing = false
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

    private fun observeSetBagBadge() {
        productsViewModel.addProductResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
                Utils.setupCartItemsBadge(tv, productsInBagNumber.value!!)
            }
        })
        productsViewModel.removeProductResponse.observe(viewLifecycleOwner, {
            if(it.data!!) {
                val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
                Utils.setupCartItemsBadge(tv, productsInBagNumber.value!!)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_toolbar_menu, menu)
        menuItem = menu.findItem(R.id.shoppingCart_menuItem)
        val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
        Utils.setupCartItemsBadge(tv, productsInBagNumber.value!!)
        observeSetBagBadge()
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoppingCart_menuItem -> {
                if (!loginManager.loggedIn.value) {
                    findNavController().navigate(R.id.action_productsFragment_to_loginPrompt)
                } else {
                    findNavController().navigate(R.id.action_productsFragment_to_shoppingBagFragment)
                }
                true
            }
            R.id.search_menuItem -> {
                navigateToSearchFragment()
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