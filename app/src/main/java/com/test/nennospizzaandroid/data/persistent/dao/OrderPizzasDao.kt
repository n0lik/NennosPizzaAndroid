package com.test.nennospizzaandroid.data.persistent.dao

import androidx.room.*
import com.test.nennospizzaandroid.data.persistent.model.DbPizza
import io.reactivex.Observable

@Dao
interface OrderPizzasDao {

    @Transaction
    @Query("SELECT COUNT(*) FROM pizzas")
    fun getPizzasCount(): Observable<Int>

    @Transaction
    @Query("SELECT * FROM pizzas")
    fun getAllPizzas(): Observable<List<DbPizza>>

    @Transaction
    @Query("SELECT * FROM pizzas WHERE id= :id")
    fun getPizzaById(id: Long): Observable<DbPizza>

    @Transaction
    @Query("DELETE FROM pizzas WHERE id = :id")
    fun deletePizzaById(id: Long)

    @Transaction
    @Insert
    fun insert(vararg pizza: DbPizza)

    @Transaction
    @Update
    fun update(vararg pizza: DbPizza)

    @Transaction
    @Delete
    fun delete(vararg pizza: DbPizza)

    @Transaction
    @Query("DELETE FROM pizzas")
    fun deleteAll()

}