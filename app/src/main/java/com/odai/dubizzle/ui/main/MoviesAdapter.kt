package com.odai.dubizzle.ui.main

import android.view.ViewGroup
import com.odai.dubizzle.databinding.RowMovieBinding
import com.odai.dubizzle.models.Movie
import com.odai.dubizzle.ui.base.BaseAdapter
import com.odai.dubizzle.ui.base.BaseViewHolder
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class MoviesAdapter @Inject constructor() :
    BaseAdapter<Movie, MoviesAdapter.ViewHolder>(mutableListOf(), LOADING_STATE, true) {

    override fun getViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(RowMovieBinding.inflate(inflater, parent, false))
    }

    inner class ViewHolder(binding: RowMovieBinding) : BaseViewHolder<Movie>(binding) {

        override fun bind(position: Int, item: Movie?) = bind<RowMovieBinding> {
            this.movie = item
        }
    }
}
