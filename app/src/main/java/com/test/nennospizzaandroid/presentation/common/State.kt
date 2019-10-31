package com.test.nennospizzaandroid.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class State<T> {

    val show: LiveData<T> = MutableLiveData<T>()
    val error: LiveData<Throwable> = MutableLiveData<Throwable>()
    val loading: LiveData<Boolean> = MutableLiveData<Boolean>()

    fun postLoading(isLoading: Boolean = true) {
        (loading as MutableLiveData).postValue(isLoading)
    }

    fun postValue(t: T) {
        postLoading(false)
        (show as MutableLiveData).postValue(t)
    }

    fun postError(throwable: Throwable) {
        postLoading(false)
        (error as MutableLiveData).postValue(throwable)
    }

}