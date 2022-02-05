package mk.ukim.finki.eshop.ui.products

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.MyApplication
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.ProductsGridAdapter
import mk.ukim.finki.eshop.adapters.ProductsListAdapter
import mk.ukim.finki.eshop.databinding.FragmentProductsBinding
import mk.ukim.finki.eshop.ui.account.LoginManager
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import mk.ukim.finki.eshop.util.Utils.Companion.showShimmerEffect
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    @Inject
    lateinit var loginManager: LoginManager

    private val args by navArgs<ProductsFragmentArgs>()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val productsViewModel by viewModels<ProductsViewModel>()
    private var menuItemType = true
    private val mAdapterList by lazy {
        ProductsListAdapter(
            productsViewModel,
            MyApplication.loggedIn.value
        )
    }
    private val mAdapterGrid by lazy {
        ProductsGridAdapter(
            productsViewModel,
            MyApplication.loggedIn.value
        )
    }
    private lateinit var menuItem: MenuItem


    override fun onResume() {
        super.onResume()
        setupSortingDropdown()
        if (this::menuItem.isInitialized) {
            val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
            Utils.setupCartItemsBadge(tv, MyApplication.itemsInBag)
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
        observeAddOrRemoveFromWishlistResponse()
        setupSwipeToRefresh()
        return binding.root
    }

    private fun observeAddOrRemoveFromWishlistResponse() {
        productsViewModel.removeProductFromWishlistResponse.value = NetworkResult.Loading()
        productsViewModel.addProductToWishlistResponse.value = NetworkResult.Loading()
        productsViewModel.removeProductFromWishlistResponse.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkResult.Success -> {
                    productsViewModel.addOrRemoveProductFromWishlist(it.data!!, false)
                    Utils.showSnackbar(binding.root, "Removed product from wishlist!", Snackbar.LENGTH_SHORT)
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(binding.root, "Error removing product from wishlist!", Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        })
        productsViewModel.addProductToWishlistResponse.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkResult.Success -> {
                    productsViewModel.addOrRemoveProductFromWishlist(it.data!!, true)
                    Utils.showSnackbar(binding.root, "Added product to wishlist!", Snackbar.LENGTH_SHORT)
                }
                is NetworkResult.Error -> {
                    Utils.showSnackbar(binding.root, "Error adding product to wishlist!", Snackbar.LENGTH_SHORT)
                }
                else -> {}
            }
        })
    }

    private fun setupSwipeToRefresh () {
        binding.productsSwipeRefresh.setOnRefreshListener {
            getProducts()
        }
    }


    private fun setupSortingDropdown() {
        val sortingCriteria = resources.getStringArray(R.array.sorting_products)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sortingCriteria)
        binding.sortingAutocomplete.setAdapter(arrayAdapter)
        binding.sortingAutocomplete.setOnItemClickListener { _, _, position, _ ->
            productsViewModel.orderProductsByCriteria(position)
        }
    }

    private fun getProducts() {
        if (!args.searchText.isNullOrEmpty()) {
            productsViewModel.getFilteredProductsForCategory(args.searchText!!)
        }
        else if(args.filterDto != null) {
            productsViewModel.getProductsInPriceRange(args.filterDto!!)
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
                        setupProductsNotEmpty()
                        mAdapterList.setData(response.data)
                        mAdapterGrid.setData(response.data)
                    }
                    else {
                        setupProductsEmpty()
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

    private fun setupProductsEmpty() {
        binding.noProductsTextView.visibility = View.VISIBLE
        binding.shoppingBagErrorProductsLottie.visibility = View.VISIBLE
        binding.sortAndFilterConstraintLayout.visibility = View.GONE
        binding.productsSwipeRefresh.visibility = View.GONE
    }

    private fun setupProductsNotEmpty() {
        binding.noProductsTextView.visibility = View.GONE
        binding.shoppingBagErrorProductsLottie.visibility = View.GONE
        binding.sortAndFilterConstraintLayout.visibility = View.VISIBLE
        binding.productsSwipeRefresh.visibility = View.VISIBLE
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
        menuItem = menu.findItem(R.id.shoppingCart_menuItem)
        val tv = menuItem.actionView.findViewById<TextView>(R.id.cart_badge)
        Utils.setupCartItemsBadge(tv, MyApplication.itemsInBag)
        menuItem.actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoppingCart_menuItem -> {
                if (!MyApplication.loggedIn.value) {
                    findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToLoginPrompt())
                } else {
                    findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToShoppingBagFragment())
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