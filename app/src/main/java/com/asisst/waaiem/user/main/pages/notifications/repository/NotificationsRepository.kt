package com.asisst.waaiem.user.main.pages.notifications.repository

import com.asisst.waaiem.user.main.pages.notifications.firebase.NotificationsFirebase

class NotificationsRepository(private val notificationsFirebase : NotificationsFirebase) {

    fun getNotifications() = notificationsFirebase.getNotifications()
}