package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Address
import mk.ukim.finki.eshop.databinding.AddressBookRowLayoutBinding
import mk.ukim.finki.eshop.util.DiffUtil

class AddressBookAdapter: RecyclerView.Adapter<AddressBookAdapter.MyViewHolder>() {
    private var addresses = emptyList<Address>()

    class MyViewHolder(
        private val binding: AddressBookRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.address = address
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AddressBookRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentAddress = addresses[position]
        holder.bind(currentAddress)
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    fun setData(newData: List<Address>) {
        val diffUtil = DiffUtil(addresses, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        addresses = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}