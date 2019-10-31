package com.test.nennospizzaandroid.presentation

import com.test.nennospizzaandroid.presentation.viewModel.CartViewModel
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import com.test.nennospizzaandroid.presentation.viewModel.PizzaViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val presentation = module {
    viewModel { SharedMainViewModel(get(), get()) }
    viewModel { PizzaViewModel() }
    viewModel { CartViewModel(get(), get()) }
}