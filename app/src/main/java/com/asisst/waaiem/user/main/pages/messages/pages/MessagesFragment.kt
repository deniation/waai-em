package com.asisst.waaiem.user.main.pages.messages.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asisst.waaiem.R
import com.asisst.waaiem.user.main.pages.messages.adapter.MessagesPagerAdapter
import com.asisst.waaiem.user.main.pages.profile.adapter.ProfilePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.fragment_profile.*

class MessagesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messages_pager.adapter = MessagesPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(messages_tabs, messages_pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "Recent Chats"
                1-> tab.text = "Friend List"
            }
        }).attach()
    }
}