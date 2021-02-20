package com.idslogic.levelshoes.ui.home.product.filters

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idslogic.levelshoes.R

class FiltersPriceFragment : Fragment() {

    companion object {
        fun newInstance() = FiltersPriceFragment()
    }

    private lateinit var viewModel: FiltersPriceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters_price, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FiltersPriceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}