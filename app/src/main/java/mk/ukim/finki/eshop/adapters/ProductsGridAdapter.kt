package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.databinding.ProductsItemLayoutBinding
import mk.ukim.finki.eshop.ui.products.ProductsViewModel
import mk.ukim.finki.eshop.util.DiffUtil

class ProductsGridAdapter(private val vm: ProductsViewModel): RecyclerView.Adapter<ProductsGridAdapter.MyViewHolder>() {
    private var products = emptyList<Product>()

    class MyViewHolder(
        private val binding: ProductsItemLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, vm: ProductsViewModel) {
            binding.product = product
            binding.vm = vm
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductsItemLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProduct = products[position]
        holder.bind(currentProduct, vm)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setData(newData: List<Product>) {
        products = newData
        notifyDataSetChanged()
    }
}