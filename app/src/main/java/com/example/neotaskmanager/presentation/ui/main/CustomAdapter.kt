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

class CustomAdapter(context: Context, resource: Int, private val items: ArrayList<CustomItem>) :
    ArrayAdapter<CustomItem>(context, resource, items) {

    @NonNull
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(R.layout.custom_spinner_layout, parent, false)
        }
        val item = getItem(position)
        val spinnerIV = convertedView!!.findViewById<ImageView>(R.id.spinner_icon)
        val spinnerTV = convertedView.findViewById<TextView>(R.id.spinner_text)
        if (item != null) {
            spinnerIV.setImageResource(item.spinnerImage)
            spinnerTV.text = item.spinnerName
        }
        return convertedView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertedView = convertView
        if (convertedView == null) {
            convertedView = LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)
        }
        val item = getItem(position)
        val dropdownIV = convertedView!!.findViewById<ImageView>(R.id.dropdown_icon)
        val dropdownTV = convertedView.findViewById<TextView>(R.id.dropdown_text)
        if (item != null) {
            dropdownIV.setImageResource(item.spinnerImage)
            dropdownTV.text = item.spinnerName
        }
        return convertedView
    }
}
