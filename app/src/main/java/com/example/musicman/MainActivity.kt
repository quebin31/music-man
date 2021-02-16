package com.example.musicman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicman.databinding.ActivityMainBinding
import com.example.musicman.extensions.viewBinding

class MainActivity : AppCompatActivity() {

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
    }

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val navController get() = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        fun setupNavUi() {
            val appBarConfiguration = AppBarConfiguration(setOf(
                    R.id.navigationSongs, R.id.navigationPlayer))

            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.navView.setupWithNavController(navController)
        }

        setupNavUi()
    }
}