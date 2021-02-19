package com.example.musicman.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.musicman.R
import com.example.musicman.databinding.ActivityMainBinding
import com.example.musicman.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

        intent.extras?.getBoolean(KEY_SHOW_PLAYER)?.let { showPlayer ->
            if (showPlayer) navController.navigate(R.id.player_fragment)
        }
    }

    private fun setupUi() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.songs_fragment, R.id.player_fragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    companion object {
        private const val KEY_SHOW_PLAYER = "com.example.musicman.show_player"

        fun newPlayerPendingIntent(context: Context): PendingIntent? {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra(KEY_SHOW_PLAYER, true)
            }

            return TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }
}