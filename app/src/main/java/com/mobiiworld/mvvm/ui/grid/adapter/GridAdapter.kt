package com.mobiiworld.mvvm.ui.grid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mobiiworld.mvvm.R
import com.mobiiworld.mvvm.ui.grid.model.ImageModel
import kotlinx.android.synthetic.main.item_grid.view.*

class GridAdapter(
    private val list: ArrayList<ImageModel> = arrayListOf(),
    private val itemClicked: (Int, ImageModel, View) -> Unit
) :
    RecyclerView.Adapter<GridAdapter.GridViewHolder>() {


    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(imageModel: ImageModel, itemClicked: (Int, ImageModel, View) -> Unit) {
            itemView.apply {
                ViewCompat.setTransitionName(image, adapterPosition.toString())

                image.load(imageModel.image)

                setOnClickListener {
                    itemClicked.invoke(adapterPosition, imageModel,image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        return with(LayoutInflater.from(parent.context)) {
            GridViewHolder(inflate(R.layout.item_grid, parent, false))
        }
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(list[position], itemClicked)
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