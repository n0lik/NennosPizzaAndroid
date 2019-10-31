package com.test.nennospizzaandroid.presentation.viewModel

import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.data.repo.OrderRepo
import com.test.nennospizzaandroid.data.repo.PaymentRepo
import com.test.nennospizzaandroid.presentation.common.BaseViewModel
import com.test.nennospizzaandroid.presentation.common.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CartViewModel(private val paymentRepo: PaymentRepo, private val orderRepo: OrderRepo): BaseViewModel() {

    val payState = State<Unit>()

    fun payOrder(pizzas: List<PizzaItem>, drinkId: List<Long>){
        paymentRepo.pay(pizzas, drinkId)
            .andThen(orderRepo.deleteAllDrinks())
            .andThen(orderRepo.deleteAllPizzas())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ payState.postValue(Unit) }, { payState.postError(it) })
            .autoDispose()
    }

}