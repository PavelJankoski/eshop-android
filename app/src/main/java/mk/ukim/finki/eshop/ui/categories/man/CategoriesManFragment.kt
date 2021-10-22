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
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import mk.ukim.finki.eshop.util.Utils.Companion.showShimmerEffect
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
                    hideShimmerEffect(binding.manCategoriesShimmerFrameLayout, binding.manCategoriesRecyclerView)
                    if(response.data != null) {
                        mAdapter.setData(response.data.filter { it.gender.lowercase() == "men" })
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect(binding.manCategoriesShimmerFrameLayout, binding.manCategoriesRecyclerView)
                    loadDataFromCache()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect(binding.manCategoriesShimmerFrameLayout, binding.manCategoriesRecyclerView)
                }
            }
        })
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.manCategoriesRecyclerView.adapter = mAdapter
        binding.manCategoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            categoriesViewModel.readCategories.observe(viewLifecycleOwner, {table ->
                if(!table.isNullOrEmpty()) {
                    mAdapter.setData(table[0].categories.filter { it.gender.lowercase() == "men" })
                }
            })
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}