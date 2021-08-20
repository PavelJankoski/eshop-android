package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.CartItem
import mk.ukim.finki.eshop.databinding.CustomOrderProductRowBinding

class OrderProductsAdapter: RecyclerView.Adapter<OrderProductsAdapter.MyViewHolder>() {

    private var cartItems = mutableListOf<CartItem>()

    class MyViewHolder(
        private val binding: CustomOrderProductRowBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.item = cartItem
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomOrderProductRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsAdapter.MyViewHolder {
        return OrderProductsAdapter.MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderProductsAdapter.MyViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getProduct(position: Int): Int {
        return cartItems[position].product.id
    }

    fun getCartItem(position: Int): CartItem {
        return cartItems[position]
    }

    fun setData(newData: List<CartItem>) {
        cartItems = newData as MutableList<CartItem>
        notifyDataSetChanged()
    }

    fun removeProduct(position: Int) {
        cartItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertProduct(position: Int, cartItem: CartItem) {
        cartItems.add(position, cartItem)
        notifyItemInserted(position)
    }

}