package com.test.nennospizzaandroid.presentation.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {

    private val disposable = CompositeDisposable()

    fun Disposable.autoDispose(){
        disposable.add(this)
    }

    override fun onCleared() {
        disposable.clear()
    }

}