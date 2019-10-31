package com.test.nennospizzaandroid.data.repo

import com.test.nennospizzaandroid.data.model.PizzaItem
import io.reactivex.Completable

interface PaymentRepo {

    fun pay(pizzaItems: List<PizzaItem>, drinkIdList: List<Long>): Completable

}