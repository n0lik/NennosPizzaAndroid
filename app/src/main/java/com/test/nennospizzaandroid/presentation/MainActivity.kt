package com.test.nennospizzaandroid.presentation

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.presentation.viewModel.SharedMainViewModel
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.button_add_order.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.viewmodel.ext.android.viewModel

const val ADDED_CART_DELAY = 3000L
const val EMPTY_CART = 0

class MainActivity : AppCompatActivity(), OnCartAddedCallback, OnOrderCountVisibilityCallback {

    private lateinit var navController: NavController
    private val sharedMainViewModel: SharedMainViewModel by viewModel()

    private var handler: Handler? = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navController =  Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        fetchDataIfNeed(savedInstanceState)

        sharedMainViewModel.orderTotalCount.observe(this, Observer {
            showCartItemsCount(it?:EMPTY_CART)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun fetchDataIfNeed(savedInstanceState: Bundle?) {
        savedInstanceState?: sharedMainViewModel.setup()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showCartItemsCount(count: Int) {
        if(count > EMPTY_CART){
            onShowOrderCount(isShow = true)
            order_count?.text = count.toString()
        } else {
            onShowOrderCount(isShow = false)
        }
    }

    override fun showMsgOnItemAddedToCart() {
        added_to_order_layout?.visibility = View.VISIBLE
        blockingWideVisibility(true)
        handler?.postDelayed({
            added_to_order_layout?.visibility = View.GONE
            blockingWideVisibility(false)
        }, ADDED_CART_DELAY)
    }

    private fun blockingWideVisibility(isVisible: Boolean) {
        if (isVisible) {
            wide_lock_layout?.visibility = View.VISIBLE
            wide_lock_layout?.setOnClickListener { }
        } else {
            wide_lock_layout?.visibility = View.GONE
        }
    }

    override fun onShowOrderCount(isShow: Boolean) {
        order_count?.visibility = if(isShow) View.VISIBLE else View.GONE
    }

}
