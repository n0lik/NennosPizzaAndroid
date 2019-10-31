package com.test.nennospizzaandroid.presentation.fragment

import android.os.Bundle
import android.view.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.otto.Subscribe
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.presentation.OnCartAddedCallback
import com.test.nennospizzaandroid.presentation.adapters.PizzaAdapter
import com.test.nennospizzaandroid.presentation.common.BaseFragment
import com.test.nennospizzaandroid.presentation.util.event.EventAddToCardPizza
import com.test.nennospizzaandroid.presentation.util.event.EventOnClickPizza
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import kotlinx.android.synthetic.main.fragment_pizzas_list.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

const val EMPTY_PRICE = 0.0

class PizzasListFragment : BaseFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_pizzas_list

    private val sharedMainViewModel: SharedMainViewModel by sharedViewModel()
    private lateinit var navController: NavController
    private val onCartAddedCallback by lazy { activity as OnCartAddedCallback }

    private var basePizzaPrice = EMPTY_PRICE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        fab?.setOnClickListener {
            val bundle = Bundle()
            bundle.putDouble(ARG_PIZZA_BASE_PRICE, basePizzaPrice)
            navController.navigate(R.id.pizza_fragment, bundle)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subscribeForIngredients()

        subscribeForPizzas()

    }


    private fun subscribeForPizzas() {
        with(sharedMainViewModel.pizzas) {
            show.subscribe { pizzasMain ->
                fab?.visibility = View.VISIBLE
                val ingredientsList: List<Ingredient>? =
                    sharedMainViewModel.ingredients.show.value
                ingredientsList?.let { ingredients ->
                    basePizzaPrice = pizzasMain.basePrice
                    pizzasMain.pizzas?.let { pizzasList ->
                        showPizzasList(pizzasList, ingredients, basePizzaPrice)
                    }
                }
            }
        }
    }

    private fun subscribeForIngredients() {
        with(sharedMainViewModel.ingredients) {
            loading.subscribe { loading ->
                if (loading) progress_bar?.visibility = View.VISIBLE
                else progress_bar?.visibility = View.GONE
            }
            error.subscribe {
                retry_network?.visibility = View.VISIBLE
                fab?.visibility = View.GONE
                retry_network?.setOnClickListener {
                    sharedMainViewModel.setup()
                    retry_network?.visibility = View.GONE
                    retry_network?.setOnClickListener(null)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        NennosPizzaApp.eventBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        NennosPizzaApp.eventBus.unregister(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pizzas_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_card -> {
                navController.navigate(R.id.cart_fragment)
                true
            }
            else -> false
        }
    }

    private fun showPizzasList(
        pizzas: List<PizzaItem>,
        ingredients: List<Ingredient>,
        basePrize: Double
    ) {
        pizzas_recycler_view.setHasFixedSize(true)
        pizzas_recycler_view.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        pizzas_recycler_view.adapter =
            PizzaAdapter(LayoutInflater.from(context), pizzas, ingredients, basePrize)
    }

    @Subscribe
    fun onClickPizzaItem(event: EventOnClickPizza) {
        val bundle = Bundle()
        bundle.putParcelable(
            ARG_PIZZA_ITEM,
            event.item.copy(ingredients = ArrayList(event.item.ingredients))
        )
        bundle.putDouble(ARG_PIZZA_BASE_PRICE, basePizzaPrice)
        navController.navigate(R.id.pizza_fragment, bundle)
    }

    @Subscribe
    fun onClickPizzaItemAddToCard(event: EventAddToCardPizza) {
        sharedMainViewModel.addPizzaToOrder(event.pizzaItem, event.totalPrice)
        onCartAddedCallback.showMsgOnItemAddedToCart()
    }

}