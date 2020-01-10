package co.jeanpidev.comicvine.ui

import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import co.jeanpidev.comicvine.databinding.ItemListComicBinding
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import co.jeanpidev.comicvine.R
import co.jeanpidev.comicvine.model.Issue
import co.jeanpidev.comicvine.databinding.ItemGridComicBinding

class ComicListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var comicList: List<Issue>
    private var listMode = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LIST) {
            val binding: ItemListComicBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_list_comic, parent, false)
            ListViewHolder(binding)
        } else {
            val binding: ItemGridComicBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_grid_comic, parent, false)
            GridViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_LIST) {
            (holder as ListViewHolder).bind(comicList[position])
        } else {
            (holder as GridViewHolder).bind(comicList[position])
        }
        holder.itemView.setOnClickListener {
            val intent = ComicDetailsActivity.newIntent(holder.itemView.context, comicList[position].api_detail_url)
            startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return if (::comicList.isInitialized) comicList.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (listMode) {
            TYPE_LIST
        } else {
            TYPE_GRID
        }
    }

    fun updateComicList(comicList: List<Issue>) {
        this.comicList = comicList
        notifyDataSetChanged()
    }

    fun changeViewMode() {
        listMode = !listMode
    }

    class GridViewHolder(private val binding: ItemGridComicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ComicViewModel()

        fun bind(comic: Issue) {
            viewModel.bind(comic)
            binding.viewModel = viewModel
        }
    }

    class ListViewHolder(private val binding: ItemListComicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ComicViewModel()

        fun bind(comic: Issue) {
            viewModel.bind(comic)
            binding.viewModel = viewModel
        }
    }

    companion object {
        private const val TYPE_LIST = 1
        private const val TYPE_GRID = 2
    }
}
