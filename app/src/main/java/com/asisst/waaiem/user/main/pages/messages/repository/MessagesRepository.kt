package com.asisst.waaiem.user.main.pages.messages.repository

import com.asisst.waaiem.user.main.pages.home.firebase.HomeFirebase
import com.asisst.waaiem.user.main.pages.messages.firebase.MessagesFirebase
import com.asisst.waaiem.user.main.pages.profile.firebase.ProfileFirebase

class MessagesRepository(private val firebase: MessagesFirebase) {
    fun sendMessage(message: String, recepient: String) = firebase.sendMessage(message, recepient)

    fun getConversation(recepient: String) = firebase.getConversation(recepient)

    fun getChats() = firebase.getChats()
}