package com.asisst.waaiem.user.authentication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asisst.waaiem.user.authentication.repository.AuthRepository
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.main.pages.home.repository.HomeRepository
import com.asisst.waaiem.user.main.pages.home.viewmodel.HomeViewmodel
import com.asisst.waaiem.user.main.pages.profile.repository.ProfileRepository

class HomeViewmodelFactory(private val homeRepository: HomeRepository, private val profileRepository: ProfileRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewmodel(homeRepository, profileRepository) as T
    }
}