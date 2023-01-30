package com.turun.wrd

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

class CustomAdapter(private val mButtons: ArrayList<Button>, private val mColumnWidth: Int, private val mColumnHeight: Int) : BaseAdapter() {

    override fun getCount() = mButtons.size

    override fun getItem(position: Int) = mButtons[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val button = if (convertView == null) mButtons[position] else convertView as Button

        val params = ViewGroup.LayoutParams(mColumnWidth, mColumnHeight)
        button.layoutParams = params

        return button
    }
}
