package com.test.nennospizzaandroid.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.presentation.common.BaseViewModel
import com.test.nennospizzaandroid.presentation.fragment.EMPTY_PRICE

class PizzaViewModel: BaseViewModel() {

    private val totalPrice: MutableLiveData<Double> = MutableLiveData()
    var preOrderedPizzaItem: PizzaItem? = null
    private var ingredients: List<Ingredient>? = null

    fun setup(pizzaItem: PizzaItem, ingredients: List<Ingredient>, basePrice: Double) {
        if (preOrderedPizzaItem == null) {

            this.ingredients = ingredients
            var pizzaItemIngredientsPrice = EMPTY_PRICE

            pizzaItem.ingredients.forEach {ingredientId ->
                getIngredientPriceById(ingredientId)?.let { ingredientPrice->
                    pizzaItemIngredientsPrice+=ingredientPrice
                }
            }
            totalPrice.value = calculateTotalPrice(basePrice, pizzaItemIngredientsPrice)
            preOrderedPizzaItem = pizzaItem
        }
    }

    private fun calculateTotalPrice(basePrice: Double, pizzaItemIngredientsPrice: Double)
            = basePrice + pizzaItemIngredientsPrice

    fun onIngredientChanged(isAdd: Boolean, ingredientId: Long){
        if(isAdd){
            addIngredientToPizza(ingredientId)
        } else {
            deleteIngredientFromPizza(ingredientId)
        }
    }

    private fun addIngredientToPizza(ingredientId: Long) {
        val ingredientPrice = getIngredientPriceById(ingredientId)
        if (ingredientPrice != null && preOrderedPizzaItem?.ingredients?.contains(ingredientId) == false ) {
            preOrderedPizzaItem?.ingredients?.add(ingredientId)
            val currentTotalPrice = totalPrice.value?.plus(ingredientPrice)
            totalPrice.value = currentTotalPrice
        }
    }

    private fun deleteIngredientFromPizza(ingredientId: Long) {
        val ingredientPrice = getIngredientPriceById(ingredientId)
        if (ingredientPrice!= null && preOrderedPizzaItem?.ingredients?.remove(ingredientId) == true) {
            val currentTotalPrice = totalPrice.value?.minus(ingredientPrice)
            totalPrice.value = currentTotalPrice
        }
    }

    private fun getIngredientPriceById(id:Long):Double? {
        return ingredients?.find { it.id == id }?.price
    }

    fun getTotalPrice(): LiveData<Double> {
        return totalPrice
    }
}