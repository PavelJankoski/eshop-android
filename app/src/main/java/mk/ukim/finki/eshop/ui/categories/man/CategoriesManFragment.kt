package mk.ukim.finki.eshop.ui.categories.man

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.adapters.CategoriesAdapter
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.databinding.FragmentCategoriesManBinding
import mk.ukim.finki.eshop.ui.categories.CategoriesViewModel
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils
import java.util.*

@AndroidEntryPoint
class CategoriesManFragment : Fragment() {
    private var _binding: FragmentCategoriesManBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { CategoriesAdapter() }
    private val categoriesViewModel by activityViewModels<CategoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesManBinding.inflate(inflater, container, false)
        setupRecyclerView()
        categoriesViewModel.categoriesResponse.observe(viewLifecycleOwner, {response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    if(response.data != null) {
                        mAdapter.setData(response.data.filter { it.gender.lowercase() == "male" })
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.manCategoriesRecyclerView.adapter = mAdapter
        binding.manCategoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        /*val categoriesList = arrayListOf<Category>(
            Category("T-SHIRT", "https://cdn.shopify.com/s/files/1/0267/0077/5504/products/CuomoSapiens_UnisexTshirt_HeatherGrey_NOBALONEY_Catalog_Male_PF1_1024x1024@2x.png?v=1591915279", "Male"),
            Category("JACKET", "https://officialpsds.com/imageview/rx/jx/rxjx2w_large.png?1523918242", "Male"),
            Category("SHOES", "https://lh3.googleusercontent.com/proxy/cOnJycjAj1nVOT5zDMCvdXRP1mist6qCVA6KzcRG0jvlsGhmCOPMz5gMr0L3m5dLTMGMc2P8T8aJOcvwewCW8tI11x6vmNC8p49pRF3yDnYyRX3XOSUCg2EQtWY", "Male"),
            Category("SHIRT", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSV4o7R5GDpP9WBIhOYmYysOO72VKMUE2I_3A&usqp=CAU", "Male"),
            Category("JEANS", "https://lh3.googleusercontent.com/proxy/qpFKpZwuaXD6ehJP-V62Rg3EqQ9wzoHk49uoCR1xz7ZWbCYOmmbVuK_dEbZygcMOEsRBjc1TAPG6XdtmFTVommggYgwf54zuJ9u3IsfRBEVxtW-ZYSL8-hnu33-irJO4GJ0", "Male"),
            )
        mAdapter.setData(categoriesList)*/
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            categoriesViewModel.readCategories.observe(viewLifecycleOwner, {table ->
                if(!table.isNullOrEmpty()) {
                    mAdapter.setData(table[0].categories.filter { it.gender.lowercase() == "male" })
                }
            })
        }
    }

    private fun showShimmerEffect() {
        binding.manCategoriesRecyclerView.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.manCategoriesRecyclerView.hideShimmer()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}