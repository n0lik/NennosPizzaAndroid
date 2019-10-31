package com.test.nennospizzaandroid.data.persistent.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.nennospizzaandroid.data.model.PizzaItem

@Entity(tableName = "pizzas")
data class DbPizza(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var totalPrice: Double = 0.0,

    @Embedded
    var pizza: PizzaItem

)
