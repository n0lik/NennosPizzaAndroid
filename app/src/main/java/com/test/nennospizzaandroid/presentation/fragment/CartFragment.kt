package com.test.nennospizzaandroid.presentation.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.otto.Subscribe
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.OrderItem
import com.test.nennospizzaandroid.data.model.OrderItem.Companion.DRINK
import com.test.nennospizzaandroid.data.model.OrderItem.Companion.PIZZA
import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.data.persistent.model.DbDrink
import com.test.nennospizzaandroid.data.persistent.model.DbPizza
import com.test.nennospizzaandroid.presentation.OnOrderCountVisibilityCallback
import com.test.nennospizzaandroid.presentation.adapters.OrdersAdapter
import com.test.nennospizzaandroid.presentation.common.BaseFragment
import com.test.nennospizzaandroid.presentation.util.event.EventDeleteOrder
import com.test.nennospizzaandroid.presentation.viewModel.CartViewModel
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

const val HALF_VIEW_ALPHA = 0.5f
const val DEFAULT_VIEW_ALPHA = 1.0f

class CartFragment: BaseFragment() {

    private val sharedMainViewModel: SharedMainViewModel by sharedViewModel()
    private val cartViewModel: CartViewModel by viewModel()
    private var pizzasOrderList: List<DbPizza> = ArrayList()
    private var drinksOrderList: List<DbDrink> = ArrayList()
    private lateinit var navController: NavController

    private val onOrderCountVisibilityCallback by lazy { activity as OnOrderCountVisibilityCallback }

    override val layoutResId: Int
        get() = R.layout.fragment_cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        setupOrdersList(emptyList())
        navController = Navigation.findNavController(view)

        with(sharedMainViewModel.orderedDrinks){
            show.subscribe {
                drinksOrderList = ArrayList(it)
                updateOrderList(getOrdersList())
            }
        }

        with(sharedMainViewModel.orderedPizzas){
            show.subscribe {
                pizzasOrderList = ArrayList(it)
                updateOrderList(getOrdersList())
            }
        }

        with(cartViewModel.payState) {
            show.subscribe {
                navController.popBackStack()
                navController.navigate(R.id.fragment_complete_order)
            }
            loading.subscribe { loading ->
                if (loading) progress_bar?.visibility = View.VISIBLE
                else progress_bar?.visibility = View.GONE
            }
            error.subscribe {
                Toast.makeText(context, R.string.retry_label, Toast.LENGTH_LONG).show()
            }
        }

        checkout_btn?.setOnClickListener {
            val pizzasList = ArrayList<PizzaItem>()
            pizzasOrderList.forEach {
                pizzasList.add(it.pizza)
            }
            val drinkIdsList = ArrayList<Long>()
            drinksOrderList.forEach {
                drinkIdsList.add(it.drink.drinkId)
            }
            cartViewModel.payOrder(pizzasList, drinkIdsList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_drink -> {
                navController.navigate(R.id.drinks_fragment)
                true
            }
            else -> false
        }
    }

    private fun setupOrdersList(ordersList: List<OrderItem>) {
        orders_recycler_view.isNestedScrollingEnabled = false
        orders_recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        orders_recycler_view.adapter = OrdersAdapter(
            LayoutInflater.from(context),
            ordersList
        )
    }

    private fun updateOrderList(ordersList: List<OrderItem>) {
        (orders_recycler_view.adapter as OrdersAdapter?)?.items = ordersList
        orders_recycler_view.adapter?.notifyDataSetChanged()
        updateTotalPriceBtn(ordersList)
    }

    private fun updateTotalPriceBtn(ordersList: List<OrderItem>) {
        var totalPrice = EMPTY_PRICE
        ordersList.forEach { order ->
            totalPrice += order.price
        }
        total_price_label?.text = getString(R.string.checkout_label, totalPrice.toString())


        if (totalPrice == EMPTY_PRICE) {
            checkout_btn.isEnabled = false
            checkout_btn.alpha = HALF_VIEW_ALPHA
        } else {
            checkout_btn.isEnabled = true
            checkout_btn.alpha = DEFAULT_VIEW_ALPHA
        }
    }

    private fun getOrdersList(): List<OrderItem> {
        val resultOrderList = ArrayList<OrderItem>()
        pizzasOrderList.forEach { pizzaItem ->
            resultOrderList.add(
                OrderItem(
                    PIZZA,
                    pizzaItem.id,
                    pizzaItem.pizza.name,
                    pizzaItem.totalPrice
                )
            )
        }
        drinksOrderList.forEach { drinkItem ->
            resultOrderList.add(
                OrderItem(
                    DRINK,
                    drinkItem.id,
                    drinkItem.drink.name,
                    drinkItem.drink.price
                )
            )
        }

        return resultOrderList
    }


    @Subscribe
    fun deleteOrder(event: EventDeleteOrder) {
        when (event.type) {
            PIZZA -> sharedMainViewModel.deletePizzaFromOrderById(event.id)
            DRINK -> sharedMainViewModel.deleteDrinkFromOrderById(event.id)
        }
        onOrderCountVisibilityCallback.onShowOrderCount(false)
    }

}