<<<<<<<< Updated upstream:app/src/main/java/com/example/android2m_sher_e_panjab/BottomNavigation/BottomNavigationActivity.kt
package com.example.android2m_sher_e_panjab.BottomNavigation
========
package BottomNavigation
>>>>>>>> Stashed changes:app/src/main/java/BottomNavigation/BottomNavigationActivity.kt

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
<<<<<<<< Updated upstream:app/src/main/java/com/example/android2m_sher_e_panjab/BottomNavigation/BottomNavigationActivity.kt
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
========
import androidx.navigation.findNavController
>>>>>>>> Stashed changes:app/src/main/java/BottomNavigation/BottomNavigationActivity.kt
import com.example.android2m_sher_e_panjab.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupWithNavController


class BottomNavigationActivity : AppCompatActivity() {
<<<<<<<< Updated upstream:app/src/main/java/com/example/android2m_sher_e_panjab/BottomNavigation/BottomNavigationActivity.kt
========
    @SuppressLint("MissingInflatedId")
>>>>>>>> Stashed changes:app/src/main/java/BottomNavigation/BottomNavigationActivity.kt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bottom_navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

<<<<<<<< Updated upstream:app/src/main/java/com/example/android2m_sher_e_panjab/BottomNavigation/BottomNavigationActivity.kt
        window.statusBarColor = ContextCompat.getColor(this, R.color.Main)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
========
>>>>>>>> Stashed changes:app/src/main/java/BottomNavigation/BottomNavigationActivity.kt

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_view)
        val fragmentContainer = findNavController(R.id.fragment)

        bottomNavigation.setupWithNavController(fragmentContainer)
    }
}