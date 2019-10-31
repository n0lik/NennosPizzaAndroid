package com.test.nennospizzaandroid

import android.app.Application
import com.squareup.otto.Bus
import com.squareup.otto.ThreadEnforcer
import com.test.nennospizzaandroid.data.data
import com.test.nennospizzaandroid.presentation.presentation
import org.koin.android.ext.android.startKoin

class NennosPizzaApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(data, presentation))
    }

    companion object {
        val eventBus = Bus(ThreadEnforcer.MAIN)
    }
}