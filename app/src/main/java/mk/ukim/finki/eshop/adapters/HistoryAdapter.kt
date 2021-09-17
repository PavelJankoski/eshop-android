package mk.ukim.finki.eshop.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.ShoppingCart
import mk.ukim.finki.eshop.databinding.HistoryRowLayoutBinding

class HistoryAdapter()
    : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var shoppingCarts = emptyList<ShoppingCart>()

    class MyViewHolder(
        private val binding: HistoryRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(shoppingCart: ShoppingCart) {
            binding.shoppingCart = shoppingCart
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HistoryRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(shoppingCarts[position])
    }

    override fun getItemCount(): Int {
        return shoppingCarts.size
    }

    fun setData(newData: List<ShoppingCart>) {
        shoppingCarts = newData.filterNot { cart -> cart.cartStatus.lowercase().equals("created") }
        notifyDataSetChanged()
    }
}