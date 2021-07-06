package mk.ukim.finki.eshop.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.databinding.ActivityMainBinding
import mk.ukim.finki.eshop.databinding.ActivitySearchBinding
import mk.ukim.finki.eshop.databinding.FragmentCategoriesBinding

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchBackIv.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}