package com.ankuradlakha.baseproject.ui.home.atoz

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ankuradlakha.baseproject.R

class AToZFragment : Fragment() {

    companion object {
        fun newInstance() = AToZFragment()
    }

    private lateinit var viewModel: AToZViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a_to_z, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AToZViewModel::class.java)
        // TODO: Use the ViewModel
    }

}