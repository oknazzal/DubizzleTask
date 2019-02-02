package com.odai.dubizzle.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */

operator fun CompositeDisposable?.plus(disposable: Disposable?): CompositeDisposable? {
    disposable?.let {
        this?.add(it)
    }
    return this
}
