package com.test.nennospizzaandroid.data.model

data class OrderItem(var type: Int, var id: Long, var name: String, var price: Double) {

    companion object {
        const val DRINK = 1
        const val PIZZA = 2
    }

}