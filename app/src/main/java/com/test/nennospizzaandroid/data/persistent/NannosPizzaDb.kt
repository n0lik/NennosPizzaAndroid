package com.test.nennospizzaandroid.data.persistent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.test.nennospizzaandroid.BuildConfig
import com.test.nennospizzaandroid.data.persistent.converter.Converters
import com.test.nennospizzaandroid.data.persistent.dao.OrderDrinksDao
import com.test.nennospizzaandroid.data.persistent.dao.OrderPizzasDao
import com.test.nennospizzaandroid.data.persistent.model.DbDrink
import com.test.nennospizzaandroid.data.persistent.model.DbPizza

@Database(entities = [DbDrink::class, DbPizza::class], version = BuildConfig.DB_VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NannosPizzaDb: RoomDatabase() {

        abstract val drinkDao: OrderDrinksDao
        abstract val pizzaDao: OrderPizzasDao

        companion object {

            private const val DB_NAME = "nannosPizzaDb.db"
            @Volatile
            private var instance: NannosPizzaDb? = null

            fun getInstance(context: Context): NannosPizzaDb {
                if (instance == null) {
                    synchronized(NannosPizzaDb::class) {
                        instance = create(context)
                    }
                }
                return instance!!
            }

            private fun create(context: Context): NannosPizzaDb {
                return Room.databaseBuilder(
                    context.applicationContext,
                    NannosPizzaDb::class.java,
                    DB_NAME
                )
                .build()
            }
        }
}