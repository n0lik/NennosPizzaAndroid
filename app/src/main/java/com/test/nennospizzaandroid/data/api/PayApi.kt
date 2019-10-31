package com.test.nennospizzaandroid.data.api

import io.reactivex.Completable
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded

interface PayApi {

    @FormUrlEncoded
    @POST("post")
    fun pay(
        @Field(value = "drinks") drinksList: String,
        @Field(value = "pizzas") pizzasList: String): Completable

}