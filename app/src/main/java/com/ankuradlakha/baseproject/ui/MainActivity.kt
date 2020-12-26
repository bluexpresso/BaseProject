package com.ankuradlakha.baseproject.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ankuradlakha.baseproject.R
import com.ankuradlakha.baseproject.databinding.ActivityMainBinding
import com.ankuradlakha.baseproject.network.Status
import com.ankuradlakha.baseproject.network.Status.*
import com.ankuradlakha.baseproject.utils.GENDER_WOMEN
import dagger.hilt.android.AndroidEntryPoint
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
        initLandingData(binding)
    }

    private fun initLandingData(binding: ActivityMainBinding) {
        GlobalScope.launch {
            viewModel.getLandingData(viewModel.getSelectedGender() ?: GENDER_WOMEN)
        }
        viewModel.landingLiveData.observe(this, {
            when (it.status) {
                LOADING -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                SUCCESS -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }
                ERROR -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}