package com.test.nennospizzaandroid.data.repo

import com.test.nennospizzaandroid.data.model.Drink
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.Pizzas
import io.reactivex.Single

interface FoodDrinkRepo {

    fun fetchDrinks(): Single<List<Drink>>

    fun fetchIngredients(): Single<List<Ingredient>>

    fun fetchPizzas(): Single<Pizzas>

}