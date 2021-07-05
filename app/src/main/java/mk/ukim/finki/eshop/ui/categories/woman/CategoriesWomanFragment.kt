package mk.ukim.finki.eshop.ui.categories.woman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.CategoriesAdapter
import mk.ukim.finki.eshop.databinding.FragmentCategoriesManBinding
import mk.ukim.finki.eshop.databinding.FragmentCategoriesWomanBinding
import mk.ukim.finki.eshop.databinding.FragmentHomeWomanBinding
import mk.ukim.finki.eshop.ui.categories.CategoriesViewModel
import mk.ukim.finki.eshop.util.NetworkResult
import mk.ukim.finki.eshop.util.Utils.Companion.hideShimmerEffect
import mk.ukim.finki.eshop.util.Utils.Companion.showShimmerEffect
import java.util.*


@AndroidEntryPoint
class CategoriesWomanFragment : Fragment() {
    private var _binding: FragmentCategoriesWomanBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { CategoriesAdapter() }
    private val categoriesViewModel by activityViewModels<CategoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesWomanBinding.inflate(inflater, container, false)
        setupRecyclerView()
        categoriesViewModel.categoriesResponse.observe(viewLifecycleOwner, {response ->
            when(response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect(binding.womanCategoriesShimmerFrameLayout, binding.womanCategoriesRecyclerView)
                    if(response.data != null) {
                        mAdapter.setData(response.data.filter { it.gender.lowercase() == "female" })
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect(binding.womanCategoriesShimmerFrameLayout, binding.womanCategoriesRecyclerView)
                    loadDataFromCache()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect(binding.womanCategoriesShimmerFrameLayout, binding.womanCategoriesRecyclerView)
                }
            }
        })
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.womanCategoriesRecyclerView.adapter = mAdapter
        binding.womanCategoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            categoriesViewModel.readCategories.observe(viewLifecycleOwner, {table ->
                if(!table.isNullOrEmpty()) {
                    mAdapter.setData(table[0].categories.filter { it.gender.lowercase() == "female" })
                }
            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}