package mk.ukim.finki.eshop.ui.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mk.ukim.finki.eshop.adapters.SearchAdapter
import mk.ukim.finki.eshop.databinding.ActivitySearchBinding
import mk.ukim.finki.eshop.ui.qrcode.QrCodeActivity
import mk.ukim.finki.eshop.util.Constants.Companion.PRODUCT_CODE_EXTRAS
import mk.ukim.finki.eshop.util.Constants.Companion.SEARCH_HISTORY_EXTRAS

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()
    private val mAdapter by lazy { SearchAdapter(searchViewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSearchRecyclerView()
        setupClickListeners()
        searchInputEnterListener()
        observeProductChange()
        searchViewModel.readSearchHistory.observe(this, {table ->
            if(table.isNullOrEmpty()) {
                searchHistoryIsEmpty()
            }
            else {
                searchHistoryIsNotEmpty()
            }
            mAdapter.setData(table.sortedByDescending { it.searchedOn })
        })
    }

    private fun observeProductChange() {
        searchViewModel.product.observe(this, { product ->
            //val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        })
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val productCode = data?.getStringExtra(PRODUCT_CODE_EXTRAS)
            lifecycleScope.launch {
                searchViewModel.getProductByProductCodeSafeCall(productCode!!)
            }
        }
    }

    private fun setupClickListeners() {
        binding.searchBackIv.setOnClickListener {
            finish()
        }
        binding.clearTextView.setOnClickListener {
            searchViewModel.deleteAllSearchHistory()
        }
        binding.qrCodeImageView.setOnClickListener {
            resultLauncher.launch(Intent(this, QrCodeActivity::class.java))
        }
    }

    private fun setupSearchRecyclerView() {
        binding.searchHistoryRecyclerView.adapter = mAdapter
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun searchInputEnterListener () {
        binding.searchEditText.setOnKeyListener { _, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    if(!binding.searchEditText.text.isNullOrEmpty()) {
                        val searchText = binding.searchEditText.text.toString()
                        val intent = Intent()
                        intent.putExtra(SEARCH_HISTORY_EXTRAS, searchText)
                        setResult(Activity.RESULT_OK, intent)
                        searchViewModel.insertSearchText(searchText)
                        finish()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}