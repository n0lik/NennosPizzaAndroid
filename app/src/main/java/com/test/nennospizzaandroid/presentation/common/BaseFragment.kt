package com.test.nennospizzaandroid.presentation.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.test.nennospizzaandroid.presentation.util.extentions.hideKeyboard
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {

    abstract val layoutResId: Int
    private val actionBar by lazy { (activity as AppCompatActivity).supportActionBar }

    private val onStopCompositeDisposable = CompositeDisposable()
    private val onDestroyViewCompositeDisposable = CompositeDisposable()

    fun Disposable.untilStop() = onStopCompositeDisposable.add(this)
    fun Disposable.untilDestroyView() = onDestroyViewCompositeDisposable.add(this)

    inline fun <T> LiveData<T>.subscribe(crossinline action: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { it?.let(action) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResId, container, false)

    override fun onStop() {
        super.onStop()
        context?.hideKeyboard(view)
        onStopCompositeDisposable.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyViewCompositeDisposable.clear()
    }

    protected fun setupToolbarTitle(title: String) {
        actionBar?.title = title
    }

    protected fun showAppBar(show: Boolean) {
        if (show) actionBar?.show() else actionBar?.hide()
    }

}
