package com.test.nennospizzaandroid.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import com.test.nennospizzaandroid.data.model.Drink
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.data.model.Pizzas
import com.test.nennospizzaandroid.data.persistent.model.DbDrink
import com.test.nennospizzaandroid.data.persistent.model.DbPizza
import com.test.nennospizzaandroid.data.repo.FoodDrinkRepo
import com.test.nennospizzaandroid.data.repo.OrderRepo
import com.test.nennospizzaandroid.presentation.common.BaseViewModel
import com.test.nennospizzaandroid.presentation.common.State
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class SharedMainViewModel(
    private val foodDrinkRepo: FoodDrinkRepo,
    private val orderRepo: OrderRepo
): BaseViewModel() {

    val ingredients = State<List<Ingredient>>()
    val drinks = State<List<Drink>>()
    val pizzas = State<Pizzas>()

    val orderedPizzas = State<List<DbPizza>>()
    val orderedDrinks = State<List<DbDrink>>()

    val orderTotalCount = MutableLiveData<Int>()

    fun setup(){

        fetchDrinks()
        fetchPizzasIngredients()

        handleOrderedPizzasList()
        handleOrderedDrinksList()
        handleOrderTotalCount()

    }

    private fun fetchDrinks() {
        foodDrinkRepo.fetchDrinks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ drinks.postValue(it) }, { drinks.postError(it) })
            .autoDispose()
    }

    private fun fetchPizzasIngredients() {
        Single.zip(
            foodDrinkRepo.fetchPizzas(),
            foodDrinkRepo.fetchIngredients(),
            BiFunction { drinksData: Pizzas, ingredientsData: List<Ingredient> ->
                drinksData to ingredientsData
            })
            .doOnSubscribe { ingredients.postLoading(true) }
            .doFinally { ingredients.postLoading(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    ingredients.postValue(it.second)
                    pizzas.postValue(it.first)
                },
                {
                    ingredients.postError(it)
                }
            )
            .autoDispose()
    }

    private fun handleOrderedPizzasList() {
        orderRepo.getAllPizzas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({orderedPizzas.postValue(it)}, {orderedPizzas.postError(it)})
            .autoDispose()
    }

    private fun handleOrderedDrinksList(){
        orderRepo.getAllDrinks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({orderedDrinks.postValue(it)}, {orderedDrinks.postError(it)})
            .autoDispose()
    }

    fun deletePizzaFromOrderById(id: Long){
        orderRepo.deletePizzaById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
            .autoDispose()
    }

    fun deleteDrinkFromOrderById(id: Long){
        orderRepo.deleteDrinkById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
            .autoDispose()
    }

    fun addPizzaToOrder(pizzaItem: PizzaItem, totalPrice: Double){

        orderRepo.addPizza(DbPizza(totalPrice = totalPrice, pizza = pizzaItem ))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
            .autoDispose()
    }

    fun addDrinkToOrder(drink: Drink){
        orderRepo.addDrink(DbDrink(drink = drink))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
            .autoDispose()
    }

    private fun handleOrderTotalCount() {
        orderRepo.getOrderTotalCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ orderTotalCount.value = it }
            .autoDispose()
    }

}