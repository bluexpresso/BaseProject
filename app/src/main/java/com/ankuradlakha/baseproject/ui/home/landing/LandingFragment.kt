package com.ankuradlakha.baseproject.ui.home.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentLandingBinding
import com.ankuradlakha.baseproject.network.Status.*
import com.ankuradlakha.baseproject.ui.MainViewModel
import com.ankuradlakha.baseproject.utils.BOX_TYPE_REGISTER_SIGN_IN
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_landing.*

class LandingFragment : Fragment() {

    companion object {
        fun newInstance() = LandingFragment()
    }

    private lateinit var viewModel: LandingViewModel
    private val activityViewModel: MainViewModel by activityViewModels()
    private lateinit var landingListAdapter: LandingListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_landing, container, false)
                    as FragmentLandingBinding
        initData()
        initDismissibleCard(binding)
        return binding.root
    }

    private fun initDismissibleCard(binding: FragmentLandingBinding) {
        landingListAdapter.onDismissibleCardActioned = {
            if (it == BOX_TYPE_REGISTER_SIGN_IN) {
                Snackbar.make(binding.root, "Feature coming soon", Snackbar.LENGTH_SHORT).show()
            }
        }
        landingListAdapter.onDismissibleCardDismissed = {
            if (it == BOX_TYPE_REGISTER_SIGN_IN) {
                landingListAdapter.removeDismissibleCards()
            }
        }
    }

    private fun initData() {
        landingListAdapter = LandingListAdapter(requireActivity())
        activityViewModel.landingLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                LOADING -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
                SUCCESS -> {
                    list_landing.adapter = landingListAdapter
                    landingListAdapter.setItems(it?.data)
                }
                ERROR -> {

                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LandingViewModel::class.java)
    }

}