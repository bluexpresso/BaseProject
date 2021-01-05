package com.ankuradlakha.baseproject.ui.home.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentProductListBinding
import com.ankuradlakha.baseproject.ui.BaseFragment
import com.ankuradlakha.baseproject.utils.CustomToolbar

class ProductListFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false)
                    as FragmentProductListBinding
        initToolbar(binding.toolbar)
        return binding.root
    }

    private fun initToolbar(toolbar: CustomToolbar) {
    }
}