package com.test.nennospizzaandroid.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.presentation.util.event.EventAddToCardPizza
import com.test.nennospizzaandroid.presentation.util.event.EventOnClickPizza
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pizza.*

internal class PizzaAdapter(
    private val inflater: LayoutInflater,
    private val items: List<PizzaItem>,
    private val ingredients: List<Ingredient>,
    private val basePizzaPrice: Double
) : RecyclerView.Adapter<PizzaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_pizza, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: PizzaItem) {

            Glide.with(itemView.context)
                .load(item.imageUrl)
                .into(pizza_image)

            pizza_title.text = item.name
            item_container.setOnClickListener {
                onItemClick(item)
            }
            val ingredientAndTotalPrice = getIngredientsAndTotalPrice(item)

            pizza_price_btn.text = "$${ingredientAndTotalPrice.second}"
            pizza_price_btn.setOnClickListener {
                NennosPizzaApp.eventBus.post(EventAddToCardPizza(item, ingredientAndTotalPrice.second))
            }
            pizza_ingredients.text = ingredientAndTotalPrice.first

        }
    }

    private fun onItemClick(item: PizzaItem) {
        NennosPizzaApp.eventBus.post(EventOnClickPizza(item))
    }

    private fun getIngredientsAndTotalPrice(item: PizzaItem): Pair<String, Double> {
        var totalPrice = basePizzaPrice
        val ingredientStrBuilder = StringBuilder()
        item.ingredients.forEach { ingredientId ->
            val currentIngredient = ingredients.find { it.id == ingredientId }
            currentIngredient?.price?.let { ingredientPrice ->
                totalPrice += ingredientPrice
            }
            currentIngredient?.name?.let { ingredientName ->
                if (ingredientStrBuilder.isNotEmpty())
                    ingredientStrBuilder.append(", ")
                ingredientStrBuilder.append(ingredientName)
            }
        }
        return Pair(ingredientStrBuilder.toString(), totalPrice)
    }
}