package com.test.nennospizzaandroid.data.api

import com.test.nennospizzaandroid.data.model.Drink
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.Pizzas
import io.reactivex.Single
import retrofit2.http.GET

interface PizzaDrinkApi {

    @GET("bins/ozt3z")
    fun  fetchIngredients(): Single<List<Ingredient>>


    @GET("bins/150da7")
    fun fetchDrinks(): Single<List<Drink>>


    @GET("bins/dokm7")
    fun fetchPizzas(): Single<Pizzas>

}