package com.test.nennospizzaandroid.data.repo

import com.test.nennospizzaandroid.data.persistent.model.DbDrink
import com.test.nennospizzaandroid.data.persistent.model.DbPizza
import io.reactivex.Completable
import io.reactivex.Observable

interface OrderRepo {
    fun getAllDrinks(): Observable<List<DbDrink>>
    fun getDrinkCount(): Observable<Int>
    fun addDrink(drink: DbDrink): Completable
    fun deleteDrink(drink: DbDrink): Completable
    fun deleteDrinkById(id:Long): Completable
    fun deleteAllDrinks(): Completable

    fun getAllPizzas(): Observable<List<DbPizza>>
    fun getPizzaCount(): Observable<Int>
    fun addPizza(pizza: DbPizza): Completable
    fun deletePizza(pizza: DbPizza): Completable
    fun deletePizzaById(id:Long): Completable
    fun deleteAllPizzas(): Completable

    fun getOrderTotalCount(): Observable<Int>

}