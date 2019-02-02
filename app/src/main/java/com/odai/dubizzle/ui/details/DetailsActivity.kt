package com.odai.dubizzle.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.odai.dubizzle.R
import com.odai.dubizzle.databinding.ActivityDetailsBinding
import com.odai.dubizzle.models.Movie
import com.odai.dubizzle.ui.base.BaseActivity
import kotlinx.android.synthetic.main.row_movie.view.*

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class DetailsActivity : BaseActivity<DetailsContract.Presenter>(), DetailsContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportStartPostponedEnterTransition()
        setContentView(R.layout.activity_details, hasToolbar = true, hasBackButton = true, isDataBinding = true)

        val movie = intent?.getParcelableExtra<Movie>(POSITION_MOVIE)
        getBinding<ActivityDetailsBinding>()?.movie = movie
        getBinding<ActivityDetailsBinding>()?.executePendingBindings()
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    companion object {

        private const val POSITION_MOVIE = "movie"

        fun start(activity: Activity, view: View, movie: Movie) {
            val pairs = arrayOf(
                Pair<View, String>(view.ivRowMovieCover, view.ivRowMovieCover.transitionName),
                Pair<View, String>(view.ivRowMovieImage, view.ivRowMovieImage.transitionName),
                Pair<View, String>(view.tvRowMovieTitle, view.tvRowMovieTitle.transitionName),
                Pair<View, String>(view.tvRowMovieDesc, view.tvRowMovieDesc.transitionName)
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs)
            val intent = Intent(view.context, DetailsActivity::class.java)
            intent.putExtra(POSITION_MOVIE, movie)
            view.context?.startActivity(intent, options.toBundle())
        }
    }
}
