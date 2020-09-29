package com.asisst.waaiem.user.main.pages.notifications.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.user.main.pages.notifications.adapter.NotificationRecyclerAdapter
import com.asisst.waaiem.user.main.pages.notifications.repository.NotificationsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NotificationsViewmodel(private val notificationsRepository: NotificationsRepository) : ViewModel() {

    var notifications_content_view : RecyclerView? = null

    var ctx : Context? = null

    var mLinearLayoutManager : LinearLayoutManager? = null


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    fun getNotifications(){
        val disposable = notificationsRepository.getNotifications()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({notifications ->
                notifications_content_view.also {
                    mLinearLayoutManager = LinearLayoutManager(ctx!!)
                    it?.layoutManager = mLinearLayoutManager
                    it?.setHasFixedSize(true)
                    it?.adapter = NotificationRecyclerAdapter(notifications, it)
                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

}