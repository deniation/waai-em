package com.asisst.waaiem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asisst.waaiem.user.authentication.pages.LoginActivity
import com.asisst.waaiem.user.main.pages.home.page.HomeFragment
import com.asisst.waaiem.user.main.pages.messages.pages.MessagesFragment
import com.asisst.waaiem.user.main.pages.notifications.pages.NotificationsFragment
import com.asisst.waaiem.user.main.pages.profile.pages.ProfileFragment
import com.asisst.waaiem.user.main.pages.trending.pages.TrendingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    //private lateinit var mUser : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        openFragment(HomeFragment())
        user_bottom_nav_id.setOnNavigationItemSelectedListener(vOnNavigationItemSelectedListener)
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth.currentUser
        if (mUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }else{

        }
    }

    private val vOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                openFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                openFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.trending -> {
                openFragment(TrendingFragment())
                return@OnNavigationItemSelectedListener true
            }
             R.id.message -> {
                 openFragment(MessagesFragment())
                 return@OnNavigationItemSelectedListener true
             }
             R.id.notifications -> {
                 openFragment(NotificationsFragment())
                 return@OnNavigationItemSelectedListener true
             }

        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.user_main_pager, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}