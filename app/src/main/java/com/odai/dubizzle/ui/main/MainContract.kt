package com.odai.dubizzle.ui.main

import com.odai.dubizzle.models.Movie
import com.odai.dubizzle.ui.base.BaseContract

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
interface MainContract {

    interface Presenter : BaseContract.Presenter {

        fun getMovies(page: Int, minYear: Number?, maxYear: Number?)
    }

    interface View : BaseContract.View {

        fun submitList(list: MutableList<Movie>)

        fun addList(list: MutableList<Movie>)
    }
}
