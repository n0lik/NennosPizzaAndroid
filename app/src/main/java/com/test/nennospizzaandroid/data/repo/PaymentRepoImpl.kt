package com.test.nennospizzaandroid.data.repo

import com.google.gson.Gson
import com.test.nennospizzaandroid.data.api.PayApi
import com.test.nennospizzaandroid.data.model.PizzaItem
import io.reactivex.Completable

class PaymentRepoImpl(
    private val api: PayApi,
    private val gson: Gson): PaymentRepo {

    override fun pay(pizzaItems: List<PizzaItem>, drinkIdList: List<Long>): Completable =
        api.pay(drinkIdList.joinToString(), gson.toJson(pizzaItems))

}