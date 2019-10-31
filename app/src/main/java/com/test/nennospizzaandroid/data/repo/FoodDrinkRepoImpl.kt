package com.test.nennospizzaandroid.data.repo

import com.test.nennospizzaandroid.data.api.PizzaDrinkApi
import com.test.nennospizzaandroid.data.model.Drink
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.Pizzas
import io.reactivex.Single

class FoodDrinkRepoImpl(private val api: PizzaDrinkApi): FoodDrinkRepo {

    override fun fetchDrinks(): Single<List<Drink>> = api.fetchDrinks()

    override fun fetchIngredients(): Single<List<Ingredient>> = api.fetchIngredients()

    override fun fetchPizzas(): Single<Pizzas> = api.fetchPizzas()

}