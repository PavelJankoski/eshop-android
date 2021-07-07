package mk.ukim.finki.eshop.ui.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.adapters.SearchAdapter
import mk.ukim.finki.eshop.api.model.Image
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.databinding.ActivityMainBinding
import mk.ukim.finki.eshop.databinding.ActivitySearchBinding
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding
import mk.ukim.finki.eshop.ui.details.DetailsActivity
import mk.ukim.finki.eshop.ui.qrcode.QrCodeActivity
import mk.ukim.finki.eshop.util.Constants.Companion.PRODUCT_CODE_EXTRAS
import mk.ukim.finki.eshop.util.Constants.Companion.QR_CODE_PRODUCT_DETAILS_EXTRAS
import mk.ukim.finki.eshop.util.Constants.Companion.SEARCH_HISTORY_EXTRAS
import mk.ukim.finki.eshop.util.Utils.Companion.showToast

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

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val productCode = data?.getStringExtra(PRODUCT_CODE_EXTRAS)
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("product", Product("Timberland", "NEW", "Best t-shirt ever",3, 5.0f, listOf(
                Image(0, "https://assets.ajio.com/medias/sys_master/root/20210403/4Zeb/606863a67cdb8c1f1474dd9a/-473Wx593H-461085141-blue-MODEL.jpg?fbclid=IwAR1Y1I9EL1_qBuVqQaQP2mTT74K1Y4khu-LPM5isfrtCdZdzS162crIyvn0"),
                Image(1, "https://cdn.shopify.com/s/files/1/2360/8505/products/round-neck-casual-mens-t-shirt_394x.jpg?fbclid=IwAR0MQn0b6ZgTWYoyFzk1jZ6NaeXAgV9IVLbQ_ExsfmhcppvulHOFli0I_Hw")
            ), 600.0, "B-12345", "Stripped T-shirt", false))
            startActivity(intent)
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