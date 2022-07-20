package com.bottomnavcomp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bottomnavcomp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if(!Prefs(this).isShown())
        navController.navigate(R.id.boardFragment)

        navController.addOnDestinationChangedListener { controller, destination, argumetns ->
            val list = arrayListOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_profile
            )
            if (list.contains(destination.id)) {
                binding.navView.visibility = View.VISIBLE
            } else {
                binding.navView.visibility = View.GONE
            }

            if (destination.id == R.id.boardFragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }
}