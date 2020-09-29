package com.asisst.waaiem.user.main.pages.home.repository

import android.net.Uri
import com.asisst.waaiem.user.main.pages.home.firebase.HomeFirebase

class HomeRepository(private val firebase: HomeFirebase) {

    fun currentUser() = firebase.currentUser()

    fun post(photo: Uri, caption: String) = firebase.post(photo, caption)

    fun getPosts() = firebase.getPosts()

    fun getFollowingState(user_id: String) = firebase.getFollowingState(user_id)

    fun getOthersTotalFollowers(user_id: String) = firebase.getOthersTotalFollowers(user_id)

    fun getOthersTotalFollowings(user_id: String) = firebase.getOthersTotalFollowings(user_id)
}