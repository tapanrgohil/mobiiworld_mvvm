package com.mobiiworld.mvvm.ui.movie.list

import com.mobiiworld.mvvm.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class EmptyItem : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //do nothing
    }

    override fun getLayout() = R.layout.item_no_data

}