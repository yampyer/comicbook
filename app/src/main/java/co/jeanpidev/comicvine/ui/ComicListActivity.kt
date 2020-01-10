package co.jeanpidev.comicvine.ui

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import co.jeanpidev.comicvine.R
import co.jeanpidev.comicvine.databinding.ActivityComicListBinding
import android.util.DisplayMetrics
import kotlin.math.roundToInt

class ComicListActivity : AppCompatActivity() {

    private lateinit var viewModel: ComicListViewModel
    private lateinit var binding: ActivityComicListBinding
    private var offset = 0L
    private val targetRowSize = 120
    private var listMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comic_list)

        viewModel = ViewModelProviders.of(this).get(ComicListViewModel::class.java)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.getLatestIssues(offset)
    }

    private fun calculateColumns(): Int {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        return (dpWidth / targetRowSize).roundToInt()
    }

    private fun initViews() {
        binding.rvComics.layoutManager = if (listMode) LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) else GridLayoutManager(this, calculateColumns())

        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.separator)!!)

        if (listMode) {
            binding.rvComics.addItemDecoration(decorator)
        } else {
            while (binding.rvComics.itemDecorationCount > 0) {
                binding.rvComics.getItemDecorationAt(0).let { binding.rvComics.removeItemDecoration(it) }
            }
        }

        setBtnDrawableState()
    }

    private fun initObservers() {
        viewModel.getErrorMessage().observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage)
        })
        viewModel.getOnClickListTrigger().observe(this, Observer {
            if (!listMode) {
                listMode = true
                viewModel.getComicListAdapter().changeViewMode()
                initViews()
                binding.rvComics.adapter = viewModel.getComicListAdapter()
            }
        })
        viewModel.getOnClickGridTrigger().observe(this, Observer {
            if (listMode) {
                listMode = false
                viewModel.getComicListAdapter().changeViewMode()
                initViews()
                binding.rvComics.adapter = viewModel.getComicListAdapter()
            }
        })
    }

    private fun setBtnDrawableState() {
        val colorList = if (listMode) ContextCompat.getColor(this@ComicListActivity, R.color.colorPrimary) else Color.BLACK
        val listDrawable = ContextCompat.getDrawable(this, R.drawable.ic_list)?.apply {
            setColorFilter(colorList, PorterDuff.Mode.SRC_IN)
        }
        binding.btnListMode.setCompoundDrawablesWithIntrinsicBounds(listDrawable, null, null, null)

        val colorGrid = if (!listMode) ContextCompat.getColor(this@ComicListActivity, R.color.colorPrimary) else Color.BLACK
        val gridDrawable = ContextCompat.getDrawable(this, R.drawable.ic_view_grid)?.apply {
            setColorFilter(colorGrid, PorterDuff.Mode.SRC_IN)
        }
        binding.btnGridMode.setCompoundDrawablesWithIntrinsicBounds(gridDrawable, null, null, null)
    }

    private fun showError(@StringRes errorMessage: Int) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }
}
