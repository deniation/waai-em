package com.asisst.waaiem.user.authentication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asisst.waaiem.user.authentication.repository.AuthRepository
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.main.pages.contacts.repository.ContactRepository
import com.asisst.waaiem.user.main.pages.home.repository.HomeRepository
import com.asisst.waaiem.user.main.pages.home.viewmodel.HomeViewmodel
import com.asisst.waaiem.user.main.pages.messages.repository.MessagesRepository
import com.asisst.waaiem.user.main.pages.messages.viewmodel.MessagesViewmodel
import com.asisst.waaiem.user.main.pages.notifications.repository.NotificationsRepository
import com.asisst.waaiem.user.main.pages.notifications.viewmodel.NotificationsViewmodel
import com.asisst.waaiem.user.main.pages.profile.repository.ProfileRepository
import com.asisst.waaiem.user.main.pages.trending.repository.TrendRepository
import com.asisst.waaiem.user.main.pages.trending.viewmodel.TrendingViewmodel

class NotificationsViewmodelFactory(private val notificationsRepository: NotificationsRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationsViewmodel(notificationsRepository) as T
    }
}