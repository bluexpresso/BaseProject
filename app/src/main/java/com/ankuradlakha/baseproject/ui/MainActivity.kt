package com.ankuradlakha.baseproject.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.ActivityMainBinding
import com.ankuradlakha.baseproject.ui.home.landing.LandingFragment
import com.ankuradlakha.baseproject.utils.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private var currentNavController: LiveData<NavController>? = null

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
//        currentNavController?.value?.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.label == "LandingFragment") {
//                window.apply {
//                    setFlags(
//                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    )
//                }
//            } else {
//                window.apply {
//                    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                }
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun initLandingData() {
        GlobalScope.launch {
            viewModel.getLandingData()
        }
    }
}