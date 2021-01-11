package com.idslogic.levelshoes.ui.onboarding

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatImageView
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Country
import com.idslogic.levelshoes.utils.loadSVG
import com.google.android.material.textview.MaterialTextView

class ChooseCountryArrayAdapter(
    context: Context,
    resource: Int,
    val objects: ArrayList<Country>
) :
    ArrayAdapter<Country>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country_dropdown, parent, false)
        val iconFlag = view.findViewById<AppCompatImageView>(R.id.icon_flag)
        val countryName = view.findViewById<MaterialTextView>(R.id.text_country)
        loadSVG(parent.context, objects[position]?.flag ?: "").into(iconFlag)
        countryName.text = objects[position]?.name
        return view
    }

    override fun getDropDownViewTheme(): Resources.Theme? {
        return super.getDropDownViewTheme()
    }

    override fun getItem(position: Int): Country {
        return objects[position]!!
    }

    override fun getCount(): Int {
        return objects.size
    }
}