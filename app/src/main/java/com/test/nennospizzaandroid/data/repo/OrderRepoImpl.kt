package com.test.nennospizzaandroid.data.repo

import com.test.nennospizzaandroid.data.persistent.NannosPizzaDb
import com.test.nennospizzaandroid.data.persistent.model.DbDrink
import com.test.nennospizzaandroid.data.persistent.model.DbPizza
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class OrderRepoImpl(private val db: NannosPizzaDb): OrderRepo {

    override fun getOrderTotalCount(): Observable<Int> =
        Observable.combineLatest(
            getDrinkCount(),
            getPizzaCount(),
            BiFunction { drinksCount, pizzasCount -> drinksCount + pizzasCount }
        )

    override fun getAllDrinks(): Observable<List<DbDrink>> = db.drinkDao.getAllDrinks()

    override fun getDrinkCount(): Observable<Int> =  db.drinkDao.getDrinksCount()

    override fun addDrink(drink: DbDrink) =
        Completable.fromCallable{
            db.drinkDao.insert(drink)
        }

    override fun deleteDrink(drink: DbDrink): Completable =
        Completable.fromCallable{
            db.drinkDao.insert()
        }

    override fun deleteDrinkById(id: Long): Completable =
        Completable.fromCallable{
            db.drinkDao.deleteDrinkById(id)
        }

    override fun deleteAllDrinks(): Completable =
        Completable.fromCallable{
            db.drinkDao.deleteAll()
        }

    override fun getAllPizzas(): Observable<List<DbPizza>> = db.pizzaDao.getAllPizzas()

    override fun getPizzaCount(): Observable<Int> = db.pizzaDao.getPizzasCount()

    override fun addPizza(pizza: DbPizza): Completable = Completable.fromCallable { db.pizzaDao.insert(pizza)}

    override fun deletePizza(pizza: DbPizza): Completable =
        Completable.fromCallable {
            db.pizzaDao.delete(pizza)
        }

    override fun deletePizzaById(id: Long): Completable =
        Completable.fromCallable {
            db.pizzaDao.deletePizzaById(id)
    }

    override fun deleteAllPizzas(): Completable =
        Completable.fromCallable {
            db.pizzaDao.deleteAll()
        }

}