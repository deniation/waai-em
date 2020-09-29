package com.asisst.waaiem.util

import android.app.Application
import com.asisst.waaiem.user.authentication.firebase.AuthFirebase
import com.asisst.waaiem.user.authentication.repository.AuthRepository
import com.asisst.waaiem.user.authentication.viewmodelfactory.*
import com.asisst.waaiem.user.main.pages.contacts.firebase.ContactFirebase
import com.asisst.waaiem.user.main.pages.contacts.repository.ContactRepository
import com.asisst.waaiem.user.main.pages.home.firebase.HomeFirebase
import com.asisst.waaiem.user.main.pages.home.repository.HomeRepository
import com.asisst.waaiem.user.main.pages.messages.firebase.MessagesFirebase
import com.asisst.waaiem.user.main.pages.messages.repository.MessagesRepository
import com.asisst.waaiem.user.main.pages.notifications.firebase.NotificationsFirebase
import com.asisst.waaiem.user.main.pages.notifications.repository.NotificationsRepository
import com.asisst.waaiem.user.main.pages.profile.firebase.ProfileFirebase
import com.asisst.waaiem.user.main.pages.profile.repository.ProfileRepository
import com.asisst.waaiem.user.main.pages.trending.firebase.TrendFirebase
import com.asisst.waaiem.user.main.pages.trending.repository.TrendRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class BaseApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))

        bind() from singleton { AuthFirebase() }
        bind() from singleton { HomeFirebase() }
        bind() from singleton { ProfileFirebase() }
        bind() from singleton { TrendFirebase() }
        bind() from singleton { ContactFirebase() }
        bind() from singleton { MessagesFirebase() }
        bind() from singleton { NotificationsFirebase() }

        bind() from singleton { AuthRepository(instance()) }
        bind() from singleton { HomeRepository(instance()) }
        bind() from singleton { ProfileRepository(instance(), instance()) }
        bind() from singleton { TrendRepository(instance()) }
        bind() from singleton { ContactRepository(instance()) }
        bind() from singleton { MessagesRepository(instance()) }
        bind() from singleton { NotificationsRepository(instance()) }

        bind() from provider { AuthViewmodelFactory(instance()) }
        bind() from provider { HomeViewmodelFactory(instance(), instance()) }
        bind() from provider { ProfileViewmodelFactory(instance()) }
        bind() from provider { NotificationsViewmodelFactory(instance()) }
        bind() from provider { ContactViewmodelFactory(instance()) }
        bind() from provider { TrendingViewmodelFactory(instance(), instance()) }
        bind() from provider { MessagesViewmodelFactory(instance()) }

    }
}