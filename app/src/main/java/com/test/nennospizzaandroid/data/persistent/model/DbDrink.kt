package com.test.nennospizzaandroid.data.persistent.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.nennospizzaandroid.data.model.Drink

@Entity(tableName = "drinks")
data class DbDrink(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @Embedded
    var drink: Drink
)