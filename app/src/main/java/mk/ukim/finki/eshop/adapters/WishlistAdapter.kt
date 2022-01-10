package mk.ukim.finki.eshop.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.OrderItem
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
        private var selectedSizeId = 0L

        fun bind(product: Product, vm: WishlistViewModel) {
            binding.product = product
            binding.vm = vm
            setupSizeDropdown(product)
            binding.wishlistBagBtn.setOnClickListener {
                vm.moveProductToShoppingBag(product.id, selectedSizeId)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WishlistRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

        private fun setupSizeDropdown(product: Product) {
            val arrayAdapter = ArrayAdapter(binding.root.context, R.layout.dropdown_item, product.sizes.map { s -> s.name })
            binding.sizeAutocomplete.setAdapter(arrayAdapter)
            binding.sizeAutocomplete.setOnItemClickListener { _, _, position, _ ->
                binding.wishlistBagBtn.isEnabled = true
                binding.wishlistBagBtn.text = "Move to bag"
                binding.wishlistBagBtn.strokeColor = ColorStateList.valueOf(ContextCompat.getColor(binding.wishlistBagBtn.context, R.color.green))
                binding.wishlistBagBtn.setTextColor(ContextCompat.getColor(binding.wishlistBagBtn.context, R.color.black))
                selectedSizeId = product.sizes[position].id
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