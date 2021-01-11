package com.idslogic.levelshoes.ui.home.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_search, container,
            false
        ) as FragmentSearchBinding
        initSearch(binding)
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        }
    }

    private fun initSearch(binding: FragmentSearchBinding) {
    }
}