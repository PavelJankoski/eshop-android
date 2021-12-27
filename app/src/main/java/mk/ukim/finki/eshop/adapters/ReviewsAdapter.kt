package mk.ukim.finki.eshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.api.model.Review
import mk.ukim.finki.eshop.databinding.ReviewsRowLayoutBinding
import mk.ukim.finki.eshop.util.DiffUtil

class ReviewsAdapter: RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>() {
    private var reviews = emptyList<Review>()

    class MyViewHolder(
        private val binding: ReviewsRowLayoutBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.review = review
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReviewsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = reviews[position]
        holder.bind(currentCategory)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun setData(newData: List<Review>) {
        val diffUtil = DiffUtil(reviews, newData)
        val diffUtilResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        reviews = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}