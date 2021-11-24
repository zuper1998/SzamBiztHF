package com.e.caffuserapp.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.e.caffuserapp.R
import com.google.android.material.navigation.NavigationView

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val drawerLayout = findViewById<DrawerLayout>(R.id.mainMenuDrawerLayout)

        findViewById<ImageView>(R.id.imageMenu).setOnClickListener() {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navigationView = findViewById<NavigationView>(R.id.navigationView)

        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navigationView, navController)
        val text = findViewById<TextView>(R.id.textMenu)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            text.text = when (destination.id) {
                R.id.HomeFragment -> "Welcome back"
                R.id.AccountFragment -> "Manage account"
                else -> ""
            }
        }
    }
}