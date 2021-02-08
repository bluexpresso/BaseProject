package com.idslogic.levelshoes.ui.home.shoppingbag

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.ui.BaseFragment

class ShoppingBagFragment : BaseFragment() {

    companion object {
        fun newInstance() = ShoppingBagFragment()
    }

    private lateinit var viewModel: ShoppingBagViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping_bag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShoppingBagViewModel::class.java)
        // TODO: Use the ViewModel
    }

}