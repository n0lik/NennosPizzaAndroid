package com.test.nennospizzaandroid.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.test.nennospizzaandroid.BuildConfig
import com.test.nennospizzaandroid.data.api.PayApi
import com.test.nennospizzaandroid.data.api.PizzaDrinkApi
import com.test.nennospizzaandroid.data.persistent.NannosPizzaDb
import com.test.nennospizzaandroid.data.repo.*
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val data = module {
    single { createGson() }
    single { createOkHttpClient(get()) }
    single { createDatabase(get()) }
    single { createWebService<PizzaDrinkApi>(get(), get(), BuildConfig.DEFAULT_SERVER_URL_INFO) }
    single { createWebService<PayApi>(get(), get(), BuildConfig.DEFAULT_SERVER_URL_PAY) }
    single<OrderRepo> { OrderRepoImpl(get()) }
    single<FoodDrinkRepo> { FoodDrinkRepoImpl(get()) }
    single<PaymentRepo> { PaymentRepoImpl(get(), get()) }
}

private fun createOkHttpClient(context: Context): OkHttpClient {
    val dispatcher = Dispatcher()
    dispatcher.maxRequests = 10
    dispatcher.maxRequestsPerHost = 5
    return OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .connectTimeout(2, TimeUnit.MINUTES)
        .callTimeout(2, TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(4, 1L, TimeUnit.MINUTES))
        .addNetworkInterceptor(HttpLoggingInterceptor())
        .addInterceptor(ChuckInterceptor(context))
        .build()
}

private inline fun <reified T> createWebService(okHttpClient: OkHttpClient, gson: Gson, url: String): T {
    val retrofit = retrofitBuilder(url, okHttpClient, gson)
        .build()
    return retrofit.create(T::class.java)
}

private fun retrofitBuilder(url: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit.Builder {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
}

private fun createGson(): Gson {
    return GsonBuilder()
        .create()
}

private fun createDatabase(context: Context): NannosPizzaDb {
    return NannosPizzaDb.getInstance(context)
}