package com.asisst.waaiem.user.authentication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asisst.waaiem.user.authentication.repository.AuthRepository
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel

class AuthViewmodelFactory(private val authRepository: AuthRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewmodel(authRepository) as T
    }
}