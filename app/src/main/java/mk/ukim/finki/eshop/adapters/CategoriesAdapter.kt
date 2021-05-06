package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Category
import mk.ukim.finki.eshop.databinding.CategoriesRowLayoutBinding
import mk.ukim.finki.eshop.util.DiffUtil

class CategoriesAdapter: RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    private var categories = emptyList<Category>()

    class MyViewHolder(
        private val binding: CategoriesRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoriesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.bind(currentCategory)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun setData(newData: List<Category>) {
        val diffUtil = DiffUtil(categories, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        categories = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}