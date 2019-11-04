package com.test.nennospizzaandroid.viewmodelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.test.nennospizzaandroid.RxSchedulersOverrideRule
import com.test.nennospizzaandroid.data.model.Drink
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.Pizzas
import com.test.nennospizzaandroid.data.repo.FoodDrinkRepo
import com.test.nennospizzaandroid.data.repo.OrderRepo
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock


class SharedMainViewModelTest {

    companion object {

        @get:ClassRule
        @JvmStatic
        val mockScheduler = RxSchedulersOverrideRule()

    }

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val foodDrinkRepoMock = mock(FoodDrinkRepo::class.java)
    private val orderRepoMock = mock(OrderRepo::class.java)

    private val getIngredientsSuccessResponse = arrayListOf(Ingredient())
    private val getDrinksSuccessResponse = arrayListOf(Drink())
    private val getPizzasSuccessResponse = Pizzas()

    private val viewModel = SharedMainViewModel(foodDrinkRepoMock, orderRepoMock)

    @Test
    fun `test_all_load_successfully`(){

        whenever(foodDrinkRepoMock.fetchDrinks()).thenReturn(Single.just(getDrinksSuccessResponse))
        whenever(foodDrinkRepoMock.fetchIngredients()).thenReturn(Single.just(getIngredientsSuccessResponse))
        whenever(foodDrinkRepoMock.fetchPizzas()).thenReturn(Single.just(getPizzasSuccessResponse))

        whenever(orderRepoMock.getAllDrinks()).thenReturn(Observable.empty())
        whenever(orderRepoMock.getAllPizzas()).thenReturn(Observable.empty())
        whenever(orderRepoMock.getOrderTotalCount()).thenReturn(Observable.just(0))

        viewModel.setup()

        verify(foodDrinkRepoMock).fetchIngredients()
        verify(foodDrinkRepoMock).fetchPizzas()
        verify(foodDrinkRepoMock).fetchDrinks()

        Assert.assertEquals(getIngredientsSuccessResponse, viewModel.ingredients.show.value)
        Assert.assertEquals(getPizzasSuccessResponse, viewModel.pizzas.show.value)
        Assert.assertEquals(getDrinksSuccessResponse, viewModel.drinks.show.value)
    }


    @Test
    fun `fail_all_loading`(){
        val throwable =  Throwable()
        whenever(foodDrinkRepoMock.fetchDrinks()).thenReturn(Single.error(throwable))
        whenever(foodDrinkRepoMock.fetchIngredients()).thenReturn(Single.error(throwable))
        whenever(foodDrinkRepoMock.fetchPizzas()).thenReturn(Single.error(throwable))

        whenever(orderRepoMock.getAllDrinks()).thenReturn(Observable.empty())
        whenever(orderRepoMock.getAllPizzas()).thenReturn(Observable.empty())
        whenever(orderRepoMock.getOrderTotalCount()).thenReturn(Observable.just(0))

        viewModel.setup()

        verify(foodDrinkRepoMock).fetchIngredients()
        verify(foodDrinkRepoMock).fetchPizzas()
        verify(foodDrinkRepoMock).fetchDrinks()

        Assert.assertEquals(throwable, viewModel.ingredients.error.value)
        Assert.assertEquals(null, viewModel.pizzas.show.value)
        Assert.assertEquals(null, viewModel.drinks.show.value)
    }


    @Test
    fun `fail_ingredients_and_pizzas_but_drink_ok`(){

        val throwable =  Throwable()

        whenever(foodDrinkRepoMock.fetchDrinks())
            .thenReturn(Single.just(getDrinksSuccessResponse))
        whenever(foodDrinkRepoMock.fetchIngredients())
            .thenReturn(Single.error(throwable))
        whenever(foodDrinkRepoMock.fetchPizzas())
            .thenReturn(Single.error(throwable))


        whenever(orderRepoMock.getAllDrinks()).thenReturn(Observable.empty())
        whenever(orderRepoMock.getAllPizzas()).thenReturn(Observable.empty())
        whenever(orderRepoMock.getOrderTotalCount()).thenReturn(Observable.just(0))

        viewModel.setup()

        verify(foodDrinkRepoMock).fetchIngredients()
        verify(foodDrinkRepoMock).fetchPizzas()
        verify(foodDrinkRepoMock).fetchDrinks()

        Assert.assertEquals(throwable, viewModel.ingredients.error.value)
        Assert.assertEquals(null, viewModel.ingredients.show.value)
        Assert.assertEquals(null, viewModel.pizzas.show.value)

        Assert.assertEquals(getDrinksSuccessResponse, viewModel.drinks.show.value)

    }

}