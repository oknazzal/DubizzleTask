package com.odai.dubizzle.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.odai.dubizzle.R
import com.odai.dubizzle.data.network.HttpError
import com.odai.dubizzle.models.Movie
import com.odai.dubizzle.ui.base.BaseActivity
import com.odai.dubizzle.ui.details.DetailsActivity
import com.odai.dubizzle.ui.filter.FilterDialog
import com.odai.dubizzle.utils.click
import com.odai.dubizzle.utils.noDataOrFailedButtonClick
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Project: OdaiProject
 * Created: February 02, 2019
 *
 * @author Odai Nazzal
 */
class MainActivity : BaseActivity<MainContract.Presenter>(), MainContract.View {

    private var minYear: Number? = 1914
    private var maxYear: Number? = 2019
    private var page: Int = 1

    @set:Inject
    lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main, true)

        rvActivityMain.adapter = adapter
        adapter.setOnLoadMoreListener { page ->
            this.page = page
            presenter?.getMovies(page, minYear, maxYear)
        }
        adapter.noDataOrFailedButtonClick {
            presenter?.getMovies(page, minYear, maxYear)
        }
        adapter.showProgress()

        rlActivityMain.addOnRefreshListener {
            presenter?.getMovies(INIT_PAGE, minYear, maxYear)
        }

        adapter.click { view, movie, _ ->
            DetailsActivity.start(this, view, movie)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.iMenuActivityMainFilter -> {
                FilterDialog(this, minYear, maxYear) { minYear, maxYear ->
                    this.minYear = minYear
                    this.maxYear = maxYear
                    adapter.resetPagination()
                    page = 1
                    presenter?.getMovies(page, minYear, maxYear)
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun submitList(list: MutableList<Movie>) {
        adapter.submitList(list)
    }

    override fun addList(list: MutableList<Movie>) {
        if (list.size == 0) {
            rvActivityMain?.post {
                adapter.setPagingFinish(true)
                adapter.hideProgress()
            }
            return
        }
        adapter.add(list)
    }

    override fun handleDataError(error: HttpError) {
        super.handleDataError(error)
        adapter.handleDataError(error)
    }

    companion object {

        private const val INIT_PAGE = 1
    }
}
