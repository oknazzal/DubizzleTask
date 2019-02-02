package com.odai.dubizzle.ui.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
abstract class BasePresenter<V : BaseContract.View> protected constructor(
    protected var view: V?
) : BaseContract.Presenter {

    var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCreate() {
    }

    override fun onViewInflated() {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
        this.view = null
        compositeDisposable?.dispose()
        compositeDisposable = null
    }

    @Suppress("unused")
    companion object {

        val EMPTY: EmptyPresenter by lazy { EmptyPresenter() }
    }

    class EmptyPresenter : BasePresenter<BaseContract.View>(null)
}
