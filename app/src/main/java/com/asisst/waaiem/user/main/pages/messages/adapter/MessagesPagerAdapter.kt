package com.asisst.waaiem.user.main.pages.messages.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.asisst.waaiem.user.main.pages.contacts.pages.FriendsFragment
import com.asisst.waaiem.user.main.pages.messages.pages.ChatListFragment
import com.asisst.waaiem.user.main.pages.profile.pages.InMyKitchenFragment
import com.asisst.waaiem.user.main.pages.profile.pages.InvitedKitchensFragment
import com.asisst.waaiem.user.main.pages.profile.pages.SettingsFragment

class MessagesPagerAdapter(fm: FragmentManager?, lifecycle: Lifecycle): FragmentStateAdapter(fm!!, lifecycle) {
    private val int_items = 2

    override fun getItemCount(): Int {
        return int_items
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ChatListFragment()
            1 -> fragment = FriendsFragment()
        }
        return fragment!!
    }
}