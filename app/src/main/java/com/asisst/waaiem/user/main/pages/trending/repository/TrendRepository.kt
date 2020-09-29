package com.asisst.waaiem.user.main.pages.trending.repository

import com.asisst.waaiem.user.main.pages.home.firebase.HomeFirebase
import com.asisst.waaiem.user.main.pages.profile.firebase.ProfileFirebase
import com.asisst.waaiem.user.main.pages.trending.firebase.TrendFirebase

class TrendRepository(private val trendFirebase: TrendFirebase) {

    fun getHashtags() = trendFirebase.getHashtags()
}