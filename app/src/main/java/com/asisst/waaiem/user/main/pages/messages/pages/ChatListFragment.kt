package com.asisst.waaiem.user.main.pages.messages.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentChatListBinding
import com.asisst.waaiem.user.authentication.viewmodelfactory.MessagesViewmodelFactory
import com.asisst.waaiem.user.authentication.viewmodelfactory.NotificationsViewmodelFactory
import com.asisst.waaiem.user.main.pages.messages.viewmodel.MessagesViewmodel
import kotlinx.android.synthetic.main.fragment_chat_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ChatListFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<MessagesViewmodelFactory>()

    private lateinit var viewModel: MessagesViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentChatListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(MessagesViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.ctx = this.context
        viewModel.activity = this.activity
        viewModel.msg_content_view = chats_view

        viewModel.getChats()
    }
}