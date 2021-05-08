package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.databinding.ProductsItemLayoutBinding
import mk.ukim.finki.eshop.databinding.ProductsRowLayoutBinding
import mk.ukim.finki.eshop.util.DiffUtil

class ProductsListAdapter(): RecyclerView.Adapter<ProductsListAdapter.MyViewHolder>() {
    private var products = emptyList<Product>()

    class MyViewHolder(
        private val binding: ProductsRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = products[position]
        holder.bind(currentProduct)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setData(newData: List<Product>) {
        val diffUtil = DiffUtil(products, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        products = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}