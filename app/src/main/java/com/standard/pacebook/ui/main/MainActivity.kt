package com.standard.pacebook.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.standard.pacebook.R
import com.standard.pacebook.databinding.ActivityMainBinding
import com.standard.pacebook.ui.community.CommunityFragment
import com.standard.pacebook.ui.home.HomeFragment
import com.standard.pacebook.ui.mypage.MypageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        // Set initial fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, HomeFragment())
            .commit()
        binding.mainBnv.selectedItemId = R.id.homeFragment

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.communityFragment -> {
                    replaceFragment(CommunityFragment())
                    true
                }
                R.id.homeFragment -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.mypageFragment -> {
                    replaceFragment(MypageFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }
}