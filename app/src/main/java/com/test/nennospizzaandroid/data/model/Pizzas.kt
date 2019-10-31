package com.test.nennospizzaandroid.data.model

import com.google.gson.annotations.SerializedName

data class Pizzas(

    @field:SerializedName("pizzas")
    val pizzas: List<PizzaItem>? = null,

    @field:SerializedName("basePrice")
    val basePrice: Double = 0.0

)