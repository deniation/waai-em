package com.asisst.waaiem.user.authentication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asisst.waaiem.user.authentication.repository.AuthRepository
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.main.pages.home.repository.HomeRepository
import com.asisst.waaiem.user.main.pages.home.viewmodel.HomeViewmodel
import com.asisst.waaiem.user.main.pages.profile.repository.ProfileRepository
import com.asisst.waaiem.user.main.pages.profile.viewmodel.ProfileViewmodel

class ProfileViewmodelFactory(private val profileRepository: ProfileRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewmodel(profileRepository) as T
    }
}