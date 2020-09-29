package com.asisst.waaiem.user.main.pages.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.asisst.waaiem.user.main.pages.profile.pages.InMyKitchenFragment
import com.asisst.waaiem.user.main.pages.profile.pages.InvitedKitchensFragment
import com.asisst.waaiem.user.main.pages.profile.pages.SettingsFragment

class ProfilePagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle): FragmentStateAdapter(fm!!, lifecycle) {
    private val int_items = 3

    override fun getItemCount(): Int {
        return int_items
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = InMyKitchenFragment()
            1 -> fragment = InvitedKitchensFragment()
            2 -> fragment = SettingsFragment()
        }
        return fragment!!
    }
}