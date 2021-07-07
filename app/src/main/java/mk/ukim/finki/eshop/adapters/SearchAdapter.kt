package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.data.model.SearchEntity
import mk.ukim.finki.eshop.databinding.CategoriesRowLayoutBinding
import mk.ukim.finki.eshop.databinding.SearchRowLayoutBinding
import mk.ukim.finki.eshop.ui.search.SearchViewModel
import mk.ukim.finki.eshop.ui.wishlist.WishlistViewModel
import mk.ukim.finki.eshop.util.DiffUtil

class SearchAdapter(private val vm: SearchViewModel): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    private var searchTexts = emptyList<SearchEntity>()

    class MyViewHolder(
        private val binding: SearchRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(search: SearchEntity, vm: SearchViewModel) {
            binding.search = search
            binding.vm = vm
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSearch = searchTexts[position]
        holder.bind(currentSearch, vm)
    }

    override fun getItemCount(): Int {
        return searchTexts.size
    }

    fun setData(newData: List<SearchEntity>) {
        val diffUtil = DiffUtil(searchTexts, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        searchTexts = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}