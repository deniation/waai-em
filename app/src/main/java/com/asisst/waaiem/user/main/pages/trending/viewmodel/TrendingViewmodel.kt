package com.asisst.waaiem.user.main.pages.trending.viewmodel

import android.app.Activity
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asisst.metimesocial.ym.user.adapter.TrendRecyclerAdapter
import com.asisst.waaiem.user.main.pages.contacts.adapter.ContactsPreviewRecyclerAdapter
import com.asisst.waaiem.user.main.pages.contacts.repository.ContactRepository
import com.asisst.waaiem.user.main.pages.trending.repository.TrendRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TrendingViewmodel(private val trendRepository: TrendRepository, private val contactRepository: ContactRepository) : ViewModel() {

    var trend_content_view : RecyclerView? = null
    var selected_trend_content_view : RecyclerView? = null
    var user_preview_content_view : RecyclerView? = null
    var all_user_content_view : RecyclerView? = null

    var more_options : ConstraintLayout? = null
    var selectedTrend: CoordinatorLayout? = null

    private val disposables = CompositeDisposable()

    var mLinearLayoutManager : LinearLayoutManager? = null

    var activity : Activity? = null

    var ctx : Context? = null

    val user by lazy {
        contactRepository.currentUser()?.uid
    }

    fun getHashtags(){
        val disposable = trendRepository.getHashtags()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({hashtags ->
                trend_content_view.also {
                    mLinearLayoutManager = LinearLayoutManager(ctx!!)
                    mLinearLayoutManager!!.reverseLayout = true
                    mLinearLayoutManager!!.stackFromEnd = true
                    it?.layoutManager = mLinearLayoutManager
                    it?.setHasFixedSize(true)
                    it?.adapter = TrendRecyclerAdapter(hashtags, more_options!!, selectedTrend!!, selected_trend_content_view!!)

                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getContacts(){
        val disposable = contactRepository.getContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({contacts ->
                /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx!!, LinearLayoutManager)*/
                user_preview_content_view.also {
                    it?.layoutManager = LinearLayoutManager(ctx!!, LinearLayoutManager.HORIZONTAL, false)
                    it?.setHasFixedSize(true)
                    it?.adapter = ContactsPreviewRecyclerAdapter(contacts, user)

                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getMoreContacts(){
        val disposable = contactRepository.getContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({contacts ->
                all_user_content_view.also {
                    it?.layoutManager = LinearLayoutManager(ctx!!)
                    it?.setHasFixedSize(true)
                    it?.adapter = ContactsPreviewRecyclerAdapter(contacts, user)

                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

}