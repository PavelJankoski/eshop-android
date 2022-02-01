package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import mk.ukim.finki.eshop.R
import mk.ukim.finki.eshop.api.model.OrderHistoryItem
import mk.ukim.finki.eshop.databinding.OrderHistoryRowLayoutBinding
import mk.ukim.finki.eshop.ui.orderhistory.OrderHistoryFragmentDirections
import mk.ukim.finki.eshop.util.DiffUtil


class OrderHistoryAdapter : RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder>() {
    private var orderHistoryItems = emptyList<OrderHistoryItem>()

    class MyViewHolder(
        private val binding: OrderHistoryRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderHistoryItem: OrderHistoryItem) {
            binding.orderHistoryItem = orderHistoryItem
            binding.viewButton.setOnClickListener {
                binding.viewButton.findNavController().navigate(
                    OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderHistoryDetailsFragment(
                        orderHistoryItem.orderId
                    )
                )
            }
            setImagesInLinearLayout(orderHistoryItem)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrderHistoryRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

        private fun setImagesInLinearLayout(orderHistoryItem: OrderHistoryItem) {
            orderHistoryItem.imageUrls.forEach {
                val imageView = ImageView(binding.root.context)
                imageView.load(it) {
                    crossfade(600)
                    error(R.drawable.ic_placeholder_image)
                    placeholder(R.drawable.ic_placeholder_image)
                }
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                //
                binding.imageUrlsLinearLayout.addView(imageView)
                imageView.layoutParams.height = 400
                imageView.layoutParams.width = 250
                (imageView.layoutParams as ViewGroup.MarginLayoutParams).marginEnd = 16
                imageView.requestLayout()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = orderHistoryItems[position]
        holder.bind(currentCategory)
    }

    override fun getItemCount(): Int {
        return orderHistoryItems.size
    }

    fun setData(newData: List<OrderHistoryItem>) {
        val diffUtil = DiffUtil(orderHistoryItems, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        orderHistoryItems = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}