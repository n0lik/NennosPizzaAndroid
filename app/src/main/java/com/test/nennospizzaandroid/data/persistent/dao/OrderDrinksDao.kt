package com.test.nennospizzaandroid.data.persistent.dao

import androidx.room.*
import com.test.nennospizzaandroid.data.persistent.model.DbDrink
import io.reactivex.Observable

@Dao
interface OrderDrinksDao {

    @Transaction
    @Query("SELECT COUNT(*) FROM drinks")
    fun getDrinksCount(): Observable<Int>

    @Transaction
    @Query("SELECT * FROM drinks")
    fun getAllDrinks(): Observable<List<DbDrink>>

    @Transaction
    @Query("SELECT * FROM drinks WHERE name=:name")
    fun getDrinkByName(name: String): Observable<List<DbDrink>>

    @Transaction
    @Query("SELECT * FROM drinks WHERE drinkId=:id")
    fun getDrinkById(id: Int): Observable<List<DbDrink>>

    @Transaction
    @Query("DELETE FROM drinks WHERE id = :id")
    fun deleteDrinkById(id: Long)

    @Transaction
    @Insert
    fun insert(vararg drink: DbDrink)

    @Transaction
    @Update
    fun update(vararg drink: DbDrink)

    @Transaction
    @Delete
    fun delete(vararg drink: DbDrink)

    @Transaction
    @Query("DELETE FROM drinks")
    fun deleteAll()
}