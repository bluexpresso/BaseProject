package com.ankuradlakha.baseproject.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.ActivityMainBinding
import com.ankuradlakha.baseproject.network.Status.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initBottomNavigation()
        initLandingData()
    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottom_navigation.setupWithNavController(navHostFragment.navController)
    }

    private fun initLandingData() {
        GlobalScope.launch {
            viewModel.getLandingData()
        }
    }
}