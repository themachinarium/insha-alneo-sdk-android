package com.machinarum.alneo_sdk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.machinarum.alneo_sdk.R
import com.machinarum.alneo_sdk.databinding.ActivityMainBinding
import com.machinarum.alneo_sdk.utils.EventBus
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.paymentProcessFragment,
                R.id.paymentQRProcessFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        subscribeToObservables(navController)

    }

    private fun subscribeToObservables(navController: NavController) {
        lifecycleScope.launch {
            EventBus.navigateDeepLink.observe(this@MainActivity) {
                it?.let {
                    if (navController.graph.hasDeepLink(it)) {
                        navController.navigate(it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            EventBus.closeDeepLink.observe(this@MainActivity) {
                if (it) {
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}