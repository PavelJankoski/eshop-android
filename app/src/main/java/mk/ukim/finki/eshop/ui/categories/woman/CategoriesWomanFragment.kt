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
                    hideShimmerEffect()
                    if(response.data != null) {
                        mAdapter.setData(response.data.filter { it.gender.lowercase() == "female" })
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
        binding.womanCategoriesRecyclerView.adapter = mAdapter
        binding.womanCategoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        /*mAdapter.setData(categoriesList)*/
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

    private fun showShimmerEffect() {
        binding.womanCategoriesShimmerFrameLayout.startShimmer()
        binding.womanCategoriesShimmerFrameLayout.visibility = View.VISIBLE
        binding.womanCategoriesRecyclerView.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        if(binding.womanCategoriesShimmerFrameLayout.isShimmerVisible) {
            binding.womanCategoriesShimmerFrameLayout.visibility = View.GONE
            binding.womanCategoriesShimmerFrameLayout.stopShimmer()
        }
        binding.womanCategoriesRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}