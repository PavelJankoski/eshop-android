package mk.ukim.finki.eshop.ui.shoppingBag.customOrder.swipeToDelete

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mk.ukim.finki.eshop.adapters.OrderProductsAdapter

class SwipeGesture constructor(
    var adapter: OrderProductsAdapter
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        val productId = adapter.getProduct(position)

    }

}