package com.idslogic.levelshoes.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.idslogic.levelshoes.R
import com.idslogic.levelshoes.databinding.ActivityMainBinding
import com.idslogic.levelshoes.utils.getNoInternetDialog
import com.idslogic.levelshoes.utils.hideSoftInput
import com.idslogic.levelshoes.utils.isInternetAvailable
import com.idslogic.levelshoes.utils.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private var currentNavController: LiveData<NavController>? = null
    private var viewRoot: View? = null

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        viewRoot = binding.root
        if (savedInstanceState == null) {
            initBottomNavigation()
        }
        initLandingData()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val controller = bottom_navigation.setupWithNavController(
            listOf(
                R.navigation.nav_home,
                R.navigation.nav_a_z,
                R.navigation.nav_search, R.navigation.nav_my_account,
                R.navigation.nav_shopping_bag
            ), supportFragmentManager, R.id.nav_host_fragment, intent
        )
        currentNavController = controller
        currentNavController?.observe(this, {
            if (it.currentDestination?.label == "search_fragment") {
                viewModel.disableSearchAnimation = false
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun initLandingData() {
        if (isInternetAvailable(this)) {
            lifecycleScope.launch {
                viewModel.getLandingData()
            }
        } else {
            getNoInternetDialog(this).setPositiveButton(
                R.string.retry
            ) { _, _ ->
                initLandingData()
            }.show()
        }
    }

    override fun onBackPressed() {
        viewModel.disableSearchAnimation = true
        super.onBackPressed()
    }
}