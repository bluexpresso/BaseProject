package com.idslogic.levelshoes.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.Country
import com.idslogic.levelshoes.ui.onboarding.ChooseCountryArrayAdapter
import com.idslogic.levelshoes.utils.loadSVG
import kotlinx.android.synthetic.main.view_country_dropdown.view.*

class CountryDropdownView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        initView()
    }

    var onCountrySelected: ((Country?) -> Unit)? = null
    var selectedCountry: Country? = null
    private fun initView() {
        inflate(context, R.layout.view_country_dropdown, this)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (!choose_country_autocomplete.text.isNullOrEmpty()) {
            caption_title.visibility = View.GONE
        }
    }

    fun setCountries(countries: ArrayList<Country>) {
        val adapter =
            ChooseCountryArrayAdapter(
                context, R.layout.item_country_dropdown,
                countries
            )
        choose_country_autocomplete.setAdapter(adapter)
        choose_country_autocomplete.setOnItemClickListener { adapterView, view, i, l ->
            selectedCountry = adapter.objects[i]
            icon.visibility = View.VISIBLE
            loadSVG(context, selectedCountry?.flag ?: "").into(icon)
            choose_country_autocomplete.setText(selectedCountry?.name)
            onCountrySelected?.invoke(selectedCountry)
        }
    }

    fun enable() {
        toggleEnableDisable(true)
    }

    fun disable() {
        toggleEnableDisable(false)
    }

    private fun toggleEnableDisable(isEnable: Boolean) {
        caption_title.isEnabled = isEnabled
        text_input.isEnabled = isEnabled
        choose_country_autocomplete.isEnabled = isEnabled
        underline.isEnabled = isEnabled
    }
}