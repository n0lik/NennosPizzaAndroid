package com.test.nennospizzaandroid.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.otto.Subscribe
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.Drink
import com.test.nennospizzaandroid.presentation.OnCartAddedCallback
import com.test.nennospizzaandroid.presentation.OnOrderCountVisibilityCallback
import com.test.nennospizzaandroid.presentation.adapters.DrinksAdapter
import com.test.nennospizzaandroid.presentation.common.BaseFragment
import com.test.nennospizzaandroid.presentation.util.event.EventAddDrink
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import kotlinx.android.synthetic.main.fragment_drinks.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DrinksFragment: BaseFragment() {

    private val sharedMainViewModel: SharedMainViewModel by sharedViewModel()
    private val onCartAddedCallback by lazy { activity as OnCartAddedCallback }
    private val onOrderCountVisibilityCallback by lazy { activity as OnOrderCountVisibilityCallback }

    override val layoutResId: Int
        get() = R.layout.fragment_drinks

    override fun onResume() {
        super.onResume()
        NennosPizzaApp.eventBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        NennosPizzaApp.eventBus.unregister(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onOrderCountVisibilityCallback.onShowOrderCount(false)
        with(sharedMainViewModel.drinks) {
            show.subscribe {
                showDrinksList(it)
            }
        }
    }

    private fun showDrinksList(drinksList: List<Drink>) {
        drinks_recycler_view.isNestedScrollingEnabled = false
        drinks_recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        drinks_recycler_view.adapter = DrinksAdapter(
            LayoutInflater.from(context),
            drinksList
        )
    }

    @Subscribe
    fun drinkAdd(event: EventAddDrink) {
        sharedMainViewModel.addDrinkToOrder(event.item)
        onCartAddedCallback.showMsgOnItemAddedToCart()
        onOrderCountVisibilityCallback.onShowOrderCount(false)
    }

}