package com.test.nennospizzaandroid.data.persistent.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

class Converters {


    @TypeConverter
    fun fromString(value: String): ArrayList<Long>? {
        val listType = object : TypeToken<ArrayList<String>>() {

        }.type
        return gson.fromJson<ArrayList<Long>>(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Long>): String {
        return gson.toJson(list)
    }

    companion object{
        @JvmStatic
        val gson = Gson()
    }

}