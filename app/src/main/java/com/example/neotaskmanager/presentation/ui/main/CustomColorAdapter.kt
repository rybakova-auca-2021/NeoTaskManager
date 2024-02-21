package com.example.neotaskmanager.presentation.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.example.neotaskmanager.R

class CustomColorAdapter(context: Context, resource: Int, items: ArrayList<CustomColorItem>) :
    ArrayAdapter<CustomColorItem>(context, resource, items) {

    @NonNull
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(R.layout.custom_color_spinner_layout, parent, false)
        }
        val item = getItem(position)
        val spinnerIV = convertedView!!.findViewById<ImageView>(R.id.spinner_color)
        if (item != null) {
            spinnerIV.setImageResource(item.spinnerImage)
        }
        return convertedView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(R.layout.item_circle, parent, false)
        }
        val item = getItem(position)
        val dropdownIV = convertedView!!.findViewById<ImageView>(R.id.dropdown_icon)
        if (item != null) {
            dropdownIV.setImageResource(item.spinnerImage)
        }
        return convertedView
    }
}
