package com.test.nennospizzaandroid.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.otto.Subscribe
import com.test.nennospizzaandroid.NennosPizzaApp
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.data.model.Ingredient
import com.test.nennospizzaandroid.data.model.PizzaItem
import com.test.nennospizzaandroid.presentation.OnCartAddedCallback
import com.test.nennospizzaandroid.presentation.OnOrderCountVisibilityCallback
import com.test.nennospizzaandroid.presentation.adapters.IngredientAdapter
import com.test.nennospizzaandroid.presentation.common.BaseFragment
import com.test.nennospizzaandroid.presentation.util.event.EventPizzaIngredientChanged
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import com.test.nennospizzaandroid.presentation.viewModel.PizzaViewModel
import kotlinx.android.synthetic.main.fragment_pizza.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


const val ARG_PIZZA_ITEM = "arg_pizza_item"
const val ARG_PIZZA_BASE_PRICE = "arg_pizza_base_price"
const val DEFAULT_PIZZA_URL = ""

class PizzaFragment : BaseFragment() {

    private val sharedMainViewModel: SharedMainViewModel by sharedViewModel()

    private val pizzaViewModel: PizzaViewModel by viewModel()
    private lateinit var navController: NavController
    private var pizzaItem: PizzaItem? = null
    private var pizzaBasePrice: Double = EMPTY_PRICE
    private var totalPrice = EMPTY_PRICE

    private val onCartAddedCallback by lazy { activity as OnCartAddedCallback }
    private val onOrderCountVisibilityCallback by lazy { activity as OnOrderCountVisibilityCallback }

    override val layoutResId: Int
        get() = R.layout.fragment_pizza

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pizzaItem = it.getParcelable(ARG_PIZZA_ITEM)
            pizzaBasePrice = it.getDouble(ARG_PIZZA_BASE_PRICE, 0.0)
        }
        setupToolbarTitle(pizzaItem?.name ?: getString(R.string.custom_pizza_label))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showPizzaImage()
        with(sharedMainViewModel.ingredients) {
            show.subscribe { ingredients ->
                pizzaViewModel.setup(pizzaItem ?: getCustomPizzaItem(), ingredients, pizzaBasePrice)
                prepareIngredientList(ingredients)
            }
        }
        pizzaViewModel.getTotalPrice().subscribe { price ->
            totalPrice = price
            total_price_label.text = getString(R.string.add_to_card_label, price.toString())
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onOrderCountVisibilityCallback.onShowOrderCount(false)
        navController = Navigation.findNavController(view)

        add_to_order_btn?.setOnClickListener {
            pizzaViewModel.preOrderedPizzaItem?.let { pizzaItem ->
                sharedMainViewModel.addPizzaToOrder(pizzaItem, totalPrice)
                onCartAddedCallback.showMsgOnItemAddedToCart()
                onOrderCountVisibilityCallback.onShowOrderCount(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onOrderCountVisibilityCallback.onShowOrderCount(isShow = true)
    }

    override fun onResume() {
        super.onResume()
        NennosPizzaApp.eventBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        NennosPizzaApp.eventBus.unregister(this)
    }

    private fun getCustomPizzaItem() = PizzaItem(resources.getString(R.string.custom_pizza_label), ArrayList(), DEFAULT_PIZZA_URL)

    @Subscribe
    fun onIngredientChanged(event: EventPizzaIngredientChanged) {
        pizzaViewModel.onIngredientChanged(event.isAdded, event.ingredientId)
    }

    private fun prepareIngredientList(ingredients: List<Ingredient>) {
        ingredients_recycler_view.isNestedScrollingEnabled = false
        ingredients_recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        ingredients_recycler_view.adapter =
            IngredientAdapter(
                LayoutInflater.from(context),
                pizzaViewModel.preOrderedPizzaItem?.ingredients ?: emptyList(),
                ingredients
            )
    }

    private fun showPizzaImage() {
        pizzaItem?.imageUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .into(pizza_image)
        }
    }

}