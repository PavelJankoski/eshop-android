package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.dto.response.orderhistorydetails.OrderProduct
import mk.ukim.finki.eshop.databinding.OrderHistoryDetailsRowLayoutBinding
import mk.ukim.finki.eshop.util.DiffUtil

class OrderHistoryDetailsAdapter : RecyclerView.Adapter<OrderHistoryDetailsAdapter.MyViewHolder>() {
    private var orderHistoryDetailsItems = emptyList<OrderProduct>()

    class MyViewHolder(
        private val binding: OrderHistoryDetailsRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: OrderProduct) {
            binding.product = product
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    OrderHistoryDetailsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentOrderHistoryDetails = orderHistoryDetailsItems[position]
        holder.bind(currentOrderHistoryDetails)
    }

    override fun getItemCount(): Int {
        return orderHistoryDetailsItems.size
    }

    fun setData(newData: List<OrderProduct>) {
        val diffUtil = DiffUtil(orderHistoryDetailsItems, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        orderHistoryDetailsItems = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}