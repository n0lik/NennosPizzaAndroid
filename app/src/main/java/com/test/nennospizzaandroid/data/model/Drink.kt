package com.test.nennospizzaandroid.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Drink(

    @SerializedName("id")
    @Expose
    var drinkId: Long = 0L,

    @SerializedName("name")
    @Expose
    var name: String = "",

    @SerializedName("price")
    @Expose
    var price: Double = 0.0
)