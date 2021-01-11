package com.idslogic.levelshoes.ui.home.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentProductListBinding
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.utils.CustomToolbar

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
        toolbar.onLeftIconClick = {
            findNavController().navigateUp()
        }
        toolbar.onRightIconClick = {
            findNavController().navigate(R.id.action_to_filters)
        }
    }
}