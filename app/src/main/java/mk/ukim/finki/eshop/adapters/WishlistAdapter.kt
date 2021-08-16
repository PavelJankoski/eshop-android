package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Product
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.databinding.WishlistRowLayoutBinding
import mk.ukim.finki.eshop.ui.wishlist.WishlistViewModel
import mk.ukim.finki.eshop.util.DiffUtil

class WishlistAdapter(private val vm: WishlistViewModel): RecyclerView.Adapter<WishlistAdapter.MyViewHolder>() {
    private var products = emptyList<Product>()

    class MyViewHolder(
        private val binding: WishlistRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, vm: WishlistViewModel) {
            binding.product = product
            binding.vm = vm
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WishlistRowLayoutBinding.inflate(layoutInflater, parent, false)
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