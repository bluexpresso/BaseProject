package com.idslogic.levelshoes.ui.home.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.data.models.BaseModel
import com.idslogic.levelshoes.data.models.Product
import com.idslogic.levelshoes.databinding.FragmentLandingBinding
import com.idslogic.levelshoes.network.Status.*
import com.idslogic.levelshoes.ui.BaseFragment
import com.idslogic.levelshoes.ui.MainViewModel
import com.idslogic.levelshoes.ui.home.product.ProductDetailsFragment.Companion.ARG_PRODUCT
import com.idslogic.levelshoes.utils.BOX_TYPE_REGISTER_SIGN_IN
import com.idslogic.levelshoes.utils.LandingLinearLayoutManager
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
        initProductDetailsNavigation(binding)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hideStatusBar()
    }

    private fun initProductDetailsNavigation(binding: FragmentLandingBinding) {
        landingListAdapter.onProductSelected =
            { product: BaseModel.Hit<Product>, transitionImage: AppCompatImageView ->
                findNavController().navigate(
                    R.id.action_to_product_details, Bundle().apply {
                        putString(ARG_PRODUCT, Gson().toJson(product))
                    }, null, FragmentNavigatorExtras(
                        transitionImage to
                                transitionImage.transitionName
                    )
                )
            }
    }

    private fun initProductListNavigation(binding: FragmentLandingBinding) {
        landingListAdapter.onViewAllProducts = {
            findNavController().navigate(R.id.nav_from_home_to_product_list)
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
                    list_landing.layoutManager = LandingLinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.VERTICAL, false
                    )
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