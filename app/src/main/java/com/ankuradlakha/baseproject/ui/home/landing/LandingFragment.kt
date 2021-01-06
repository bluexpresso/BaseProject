package com.ankuradlakha.baseproject.ui.home.landing

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.FragmentLandingBinding
import com.ankuradlakha.baseproject.network.Status.*
import com.ankuradlakha.baseproject.ui.BaseFragment
import com.ankuradlakha.baseproject.ui.MainViewModel
import com.ankuradlakha.baseproject.utils.BOX_TYPE_REGISTER_SIGN_IN
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LandingFragment : BaseFragment() {

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
        initProductListNavigation(binding)
        activity?.window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        return binding.root
    }

    private fun initProductListNavigation(binding: FragmentLandingBinding) {
        landingListAdapter.onViewAllProducts = {
            findNavController().navigate(R.id.nav_from_home_to_product_list)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.apply {
            clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
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
                lifecycleScope.launch {
                    viewModel.updateCachedLandingData(landingListAdapter.mapItems)
                }
            }
        }
    }

    private fun initData() {
        landingListAdapter = LandingListAdapter(requireActivity())
        activityViewModel.landingLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                LOADING -> {
                }
                SUCCESS -> {
                    landingListAdapter.selectedCurrency = viewModel.getSelectedCurrency()
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