package com.test.nennospizzaandroid.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.test.nennospizzaandroid.R
import com.test.nennospizzaandroid.presentation.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_complete_order.*

class CompleteOrderFragment: BaseFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_complete_order

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showAppBar(false)
        navController = Navigation.findNavController(view)

        navigate_back?.setOnClickListener {
            navController.popBackStack(R.id.pizzas_list_fragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showAppBar(true)
    }

}