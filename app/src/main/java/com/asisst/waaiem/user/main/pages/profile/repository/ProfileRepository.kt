package com.asisst.waaiem.user.main.pages.profile.repository

import com.asisst.waaiem.user.main.pages.home.firebase.HomeFirebase
import com.asisst.waaiem.user.main.pages.profile.firebase.ProfileFirebase

class ProfileRepository(private val firebase: HomeFirebase, private val profileFirebase: ProfileFirebase) {

    fun getProfile() = firebase.getProfile()

    fun getClickedUserProfile(user_id : String) = firebase.getClickedUserProfile(user_id)

    fun getUserProfile() = profileFirebase.getUserProfile()

    fun getCurrentUser() = profileFirebase.currentUser()

    fun getLogOut() = profileFirebase.logout()

    fun getTotalFollowers() = profileFirebase.getTotalFollowers()

    fun getTotalFollowings() = profileFirebase.getTotalFollowings()

    fun getInMyKitchen() = profileFirebase.getInMyKitchen()

    fun getComments(postId : String) = profileFirebase.getComments(postId)
}