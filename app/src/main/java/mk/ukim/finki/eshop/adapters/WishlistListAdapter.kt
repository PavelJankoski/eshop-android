package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.data.model.WishlistEntity
import mk.ukim.finki.eshop.databinding.WishlistRowLayoutBinding
import mk.ukim.finki.eshop.ui.wishlist.WishlistViewModel
import mk.ukim.finki.eshop.util.DiffUtil

class WishlistListAdapter(private val vm: WishlistViewModel): RecyclerView.Adapter<WishlistListAdapter.MyViewHolder>() {
    private var products = emptyList<WishlistEntity>()

    class MyViewHolder(
        private val binding: WishlistRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: WishlistEntity, vm: WishlistViewModel) {
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

    fun setData(newData: List<WishlistEntity>) {
        val diffUtil = DiffUtil(products, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        products = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}