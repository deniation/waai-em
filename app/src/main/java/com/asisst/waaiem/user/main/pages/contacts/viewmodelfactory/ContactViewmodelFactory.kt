package com.asisst.waaiem.user.authentication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asisst.waaiem.user.main.pages.contacts.repository.ContactRepository
import com.asisst.waaiem.user.main.pages.contacts.viewmodel.ContactViewmodel

class ContactViewmodelFactory(private val contactRepository: ContactRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContactViewmodel(contactRepository) as T
    }
}