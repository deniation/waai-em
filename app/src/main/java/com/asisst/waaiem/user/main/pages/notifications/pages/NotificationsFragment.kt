package com.asisst.waaiem.user.main.pages.notifications.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentHomeBinding
import com.asisst.waaiem.databinding.FragmentNotificationsBinding
import com.asisst.waaiem.user.authentication.viewmodelfactory.HomeViewmodelFactory
import com.asisst.waaiem.user.authentication.viewmodelfactory.NotificationsViewmodelFactory
import com.asisst.waaiem.user.main.pages.home.viewmodel.HomeViewmodel
import com.asisst.waaiem.user.main.pages.notifications.viewmodel.NotificationsViewmodel
import kotlinx.android.synthetic.main.fragment_notifications.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class NotificationsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<NotificationsViewmodelFactory>()

    private lateinit var viewModel: NotificationsViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentNotificationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(NotificationsViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.ctx = this.context
        viewModel.notifications_content_view = notification_view
        viewModel.getNotifications()
    }

}