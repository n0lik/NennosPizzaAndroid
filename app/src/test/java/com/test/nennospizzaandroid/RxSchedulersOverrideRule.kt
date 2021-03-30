package com.test.nennospizzaandroid

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * This rule registers Scheduler hooks for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.immediate().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 */
class RxSchedulersOverrideRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setComputationSchedulerHandler { TRAMPOLINE }
                RxJavaPlugins.setIoSchedulerHandler { TRAMPOLINE }
                RxJavaPlugins.setNewThreadSchedulerHandler { TRAMPOLINE }
                RxJavaPlugins.setSingleSchedulerHandler { TRAMPOLINE }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { TRAMPOLINE }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }

    companion object {

        val TRAMPOLINE: Scheduler =
            Schedulers.trampoline()
    }

}
