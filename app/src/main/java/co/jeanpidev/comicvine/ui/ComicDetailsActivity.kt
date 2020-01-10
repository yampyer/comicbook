package co.jeanpidev.comicvine.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import co.jeanpidev.comicvine.R
import co.jeanpidev.comicvine.databinding.ActivityComicDetailsBinding
import co.jeanpidev.comicvine.model.EntityDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ComicDetailsActivity : AppCompatActivity() {
    companion object {

        private const val INTENT_URL = "entity_url"

        fun newIntent(context: Context, url: String): Intent {
            val intent = Intent(context, ComicDetailsActivity::class.java)
            intent.putExtra(INTENT_URL, url)
            return intent
        }
    }

    private lateinit var viewModel: ComicDetailsViewModel
    private lateinit var binding: ActivityComicDetailsBinding
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comic_details)

        url = intent?.extras?.getString(INTENT_URL)

        viewModel = ViewModelProviders.of(this).get(ComicDetailsViewModel::class.java)
        binding.viewModel = viewModel

        initViews()
        initObservers()

        viewModel.getIssueDetails(url!!)
    }

    private fun initViews() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initObservers() {
        viewModel.getErrorMessage().observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage)
        })
        viewModel.getIssueDetails().observe(this, Observer { response ->
            Glide.with(this)
                .load(response.issue.image.original_url)
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.broken_cover)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(binding.ivComicCover)
        })
        viewModel.getCharacters().observe(this, Observer { characters ->
            binding.rvCharacters.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvCharacters.adapter = DetailsAdapter().apply {
                val details = ArrayList<EntityDetail>()
                characters.map {
                    details.add(it.detail)
                }
                updateDetailList(details)
            }
        })
        viewModel.getLocations().observe(this, Observer { locations ->
            binding.rvLocations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvLocations.adapter = DetailsAdapter().apply {
                val details = ArrayList<EntityDetail>()
                locations.map {
                    details.add(it.detail)
                }
                updateDetailList(details)
            }
        })
        viewModel.getTeams().observe(this, Observer { teams ->
            binding.rvTeams.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvTeams.adapter = DetailsAdapter().apply {
                val details = ArrayList<EntityDetail>()
                teams.map {
                    details.add(it.detail)
                }
                updateDetailList(details)
            }
        })
    }

    private fun showError(@StringRes errorMessage: Int) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
