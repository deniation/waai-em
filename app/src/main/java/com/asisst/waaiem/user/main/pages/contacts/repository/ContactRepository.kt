package com.asisst.waaiem.user.main.pages.contacts.repository

import com.asisst.waaiem.user.main.pages.contacts.firebase.ContactFirebase

class ContactRepository(private val contactFirebase: ContactFirebase) {

    fun getContacts() = contactFirebase.getContacts()

    fun currentUser() = contactFirebase.currentUser()

    fun getFollowers() = contactFirebase.getFollowers()
}