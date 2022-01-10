package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.OrderItem
import mk.ukim.finki.eshop.databinding.ShoppingBagRowLayoutBinding
import mk.ukim.finki.eshop.util.DiffUtil

class ShoppingBagAdapter : RecyclerView.Adapter<ShoppingBagAdapter.MyViewHolder>() {
    private var orderItems = emptyList<OrderItem>()

    class MyViewHolder(
        private val binding: ShoppingBagRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderItem: OrderItem) {
            binding.orderItem = orderItem
            setupQuantityDropdown(orderItem)
            binding.quantityAutocomplete.setText(orderItem.selectedQuantity.toString(), false)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingBagRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

        private fun setupQuantityDropdown(orderItem: OrderItem) {
            val size = orderItem.sizes.find { s -> s.name == orderItem.selectedSize }
            val qtyArray: List<Int> = IntRange(1, size!!.quantity).step(1).toList()
            val arrayAdapter = ArrayAdapter(
                binding.root.context,
                R.layout.dropdown_item,
                qtyArray.map { qty -> qty.toString() })
            binding.quantityAutocomplete.setAdapter(arrayAdapter)
            binding.quantityAutocomplete.setOnItemClickListener { _, _, position, _ ->
                //
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentOrderItem = orderItems[position]
        holder.bind(currentOrderItem)
    }

    override fun getItemCount(): Int {
        return orderItems.size
    }


    fun setData(newData: List<OrderItem>) {
        val diffUtil = DiffUtil(orderItems, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        orderItems = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}