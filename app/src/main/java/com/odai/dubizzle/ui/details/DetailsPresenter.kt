package com.odai.dubizzle.ui.details

import com.odai.dubizzle.ui.base.BasePresenter

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class DetailsPresenter(view: DetailsContract.View) :
    BasePresenter<DetailsContract.View>(view),
    DetailsContract.Presenter
