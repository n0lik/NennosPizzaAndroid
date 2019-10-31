package com.test.nennospizzaandroid.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.OrderItem
import com.test.nennospizzaandroid.presentation.util.event.EventDeleteOrder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_order.*

internal class OrdersAdapter(
    private val inflater: LayoutInflater,
    var items: List<OrderItem>
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_order, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: OrderItem) {
            ingredient_title.text = item.name
            ingredient_price.text = "$${item.price}"
            delete_order_btn.setOnClickListener {
                NennosPizzaApp.eventBus.post(EventDeleteOrder(item.type, item.id))
            }
        }
    }
}