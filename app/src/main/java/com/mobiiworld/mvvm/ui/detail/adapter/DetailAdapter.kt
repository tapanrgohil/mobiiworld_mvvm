package com.mobiiworld.mvvm.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mobiiworld.mvvm.R
import com.mobiiworld.mvvm.ui.grid.model.ImageModel
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailAdapter(private val list: ArrayList<ImageModel> = arrayListOf()) :
    RecyclerView.Adapter<DetailAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(imageModel: ImageModel) {
            itemView.apply {
                image.load(imageModel.hdImage)
                tvTitle.text = imageModel.title
                tvDescription.text = imageModel.description
                tvDate.text = imageModel.date
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        return with(LayoutInflater.from(parent.context)) {
            DetailsViewHolder(inflate(R.layout.item_detail, parent, false))
        }
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateDataSet(list: List<ImageModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


}