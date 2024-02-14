package com.example.neotaskmanager.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.neotaskmanager.R
import com.example.neotaskmanager.databinding.ActivityMainBinding
import com.example.neotaskmanager.databinding.FragmentMainPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btn_trash -> {
                    navController.navigate(R.id.trashFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun hideBtmNav() {
        val navBar = findViewById<View>(R.id.bottomAppBar)
        val btn = findViewById<View>(R.id.add_button)
        navBar.visibility = View.GONE
        btn.visibility = View.GONE
    }

    fun showBtmNav() {
        val navBar = findViewById<View>(R.id.bottomAppBar)
        val btn = findViewById<View>(R.id.add_button)
        navBar.visibility = View.VISIBLE
        btn.visibility = View.VISIBLE
    }
}