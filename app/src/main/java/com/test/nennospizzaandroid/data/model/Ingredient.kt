package com.test.nennospizzaandroid.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Ingredient(

    @field:SerializedName("id")
    @Expose
    val id: Long = 0L,

    @field:SerializedName("name")
    @Expose
    val name: String = "",

    @field:SerializedName("price")
    @Expose
    val price: Double = 0.0

)