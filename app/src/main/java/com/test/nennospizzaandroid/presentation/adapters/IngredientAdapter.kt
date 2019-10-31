package com.test.nennospizzaandroid.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.presentation.util.event.EventPizzaIngredientChanged
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_ingredient.*

internal class IngredientAdapter(
    private val inflater: LayoutInflater,
    private val items: List<Long>,
    private val ingredients: List<Ingredient>
) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    internal inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(item: Ingredient) {
            ingredient_title.text = item.name
            ingredient_price.text = "$${item.price}"
            if (items.contains(item.id)) {
                addIngredientCb.isChecked = true
            }
            addIngredientCb.setOnClickListener {
                NennosPizzaApp.eventBus.post(EventPizzaIngredientChanged((it as CheckBox).isChecked, item.id, item.price))
            }
        }
    }

}