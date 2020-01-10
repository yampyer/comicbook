package co.jeanpidev.comicvine.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.jeanpidev.comicvine.R
import co.jeanpidev.comicvine.databinding.ItemDetailBinding
import co.jeanpidev.comicvine.model.EntityDetail

class DetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var detailsList: List<EntityDetail>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(detailsList[position])
    }

    override fun getItemCount(): Int {
        return if (::detailsList.isInitialized) detailsList.size else 0
    }

    fun updateDetailList(detailsList: List<EntityDetail>) {
        this.detailsList = detailsList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = DetailsViewModel()

        fun bind(detail: EntityDetail) {
            viewModel.bind(detail)
            binding.viewModel = viewModel
        }
    }
}
