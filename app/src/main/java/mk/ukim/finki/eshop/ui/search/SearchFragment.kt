package mk.ukim.finki.eshop.ui.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.adapters.SearchAdapter
import mk.ukim.finki.eshop.databinding.FragmentSearchBinding
import mk.ukim.finki.eshop.util.Utils
import okhttp3.internal.Util

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private val mAdapter by lazy { SearchAdapter(searchViewModel) }
    private val CAMERA_REQUEST_CODE = 100

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                navigateToQrCodeFragment()
            }
            else {
                Utils.showToast(requireContext(), "Cannot access qr code screen without camera permission", Toast.LENGTH_LONG)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupSearchRecyclerView()
        setupClickListeners()
        searchInputEnterListener()
        searchViewModel.readSearchHistory.observe(viewLifecycleOwner, {table ->
            if(table.isNullOrEmpty()) {
                searchHistoryIsEmpty()
            }
            else {
                searchHistoryIsNotEmpty()
            }
            mAdapter.setData(table.sortedByDescending { it.searchedOn })
        })
        return binding.root
    }

    private fun navigateToQrCodeFragment() {
        val action = SearchFragmentDirections.actionSearchFragmentToQrCodeFragment()
        findNavController().navigate(action)
    }

    private fun setupClickListeners() {
        binding.searchBackIv.setOnClickListener {
            //parentFragmentManager.popBackStack()
        }
        binding.clearTextView.setOnClickListener {
            searchViewModel.deleteAllSearchHistory()
        }
        binding.qrCodeImageView.setOnClickListener {
            if (hasPermissions(requireContext(), arrayOf(Manifest.permission.CAMERA))) {
                navigateToQrCodeFragment()
            } else {
                permReqLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA)
                )
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupSearchRecyclerView() {
        binding.searchHistoryRecyclerView.adapter = mAdapter
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun searchInputEnterListener () {
        binding.searchEditText.setOnKeyListener { _, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    if(!binding.searchEditText.text.isNullOrEmpty()) {
                        val searchText = binding.searchEditText.text.toString()

                    }
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }

    private fun searchHistoryIsEmpty() {
        binding.recentSearchesTextView.visibility = View.GONE
        binding.clearTextView.visibility = View.GONE
        binding.searchHistoryRecyclerView.visibility = View.GONE
        binding.searchHistoryIv.visibility = View.VISIBLE
        binding.searchHistoryEmptyTv.visibility = View.VISIBLE
    }

    private fun searchHistoryIsNotEmpty() {
        binding.recentSearchesTextView.visibility = View.VISIBLE
        binding.clearTextView.visibility = View.VISIBLE
        binding.searchHistoryRecyclerView.visibility = View.VISIBLE
        binding.searchHistoryIv.visibility = View.GONE
        binding.searchHistoryEmptyTv.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}