package com.example.wnsplatform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.wnsplatform.databinding.ActivityNewsBinding
import com.example.wnsplatform.news.*


class News : AppCompatActivity() {

    private var binding:ActivityNewsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setCurretFragment(HomeFragment())

        binding?.bottomNav?.setOnNavigationItemSelectedListener() {
            when(it.itemId){
                R.id.home -> setCurretFragment(HomeFragment())
                R.id.health -> setCurretFragment(HealthFragment())
                R.id.entertainment -> setCurretFragment(EntertainmentFragment())
                R.id.science -> setCurretFragment(ScienceFragment())
                R.id.sports -> setCurretFragment(SportsFragment())
            }
            true
        }
    }

    private fun setCurretFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_app,fragment)
            commit()
        }
    }




}