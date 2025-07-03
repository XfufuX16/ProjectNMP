package com.example.healingyuk.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.healingyuk.R
import com.example.healingyuk.databinding.ActivityMainBinding
import com.example.healingyuk.ui.auth.LoginActivity
import com.example.healingyuk.ui.drawer.ChangePasswordActivity
import com.example.healingyuk.ui.main.explore.ExploreFragment
import com.example.healingyuk.ui.main.favorite.FavoriteFragment
import com.example.healingyuk.ui.main.profile.ProfileFragment
import com.example.healingyuk.util.SessionManager
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    private val fragmentList = listOf(
        ExploreFragment(),
        FavoriteFragment(),
        ProfileFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Set Toolbar
        setSupportActionBar(binding.toolbarMain)

        // Setup Drawer Toggle
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbarMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Drawer header (nama dan foto profil)
        val headerView: View = binding.navView.getHeaderView(0)
        val tvNavName = headerView.findViewById<TextView>(R.id.tvNavName)
        val imgNavProfile = headerView.findViewById<ImageView>(R.id.imgNavProfile)

        tvNavName.text = "Welcome, "+session.getUserName() ?: "Guest"


        // Setup Drawer Menu Listener
        binding.navView.setNavigationItemSelectedListener(this)

        // Setup Bottom Navigation + ViewPager
        val pagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentList.size
            override fun createFragment(position: Int): Fragment = fragmentList[position]
        }
        binding.viewPager.adapter = pagerAdapter

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_explore -> binding.viewPager.currentItem = 0
                R.id.nav_favorite -> binding.viewPager.currentItem = 1
                R.id.nav_profile -> binding.viewPager.currentItem = 2
            }
            true
        }

        binding.viewPager.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.menu.getItem(position).isChecked = true
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_change_pass -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
            R.id.menu_logout -> {
                session.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (binding.viewPager.currentItem != 0) {
            binding.viewPager.currentItem = 0
        } else {
            super.onBackPressed()
        }
    }
}
