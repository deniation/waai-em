package com.asisst.waaiem.user.main.pages.contacts.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.user.main.pages.contacts.adapter.FriendsRecyclerAdapter
import com.asisst.waaiem.user.main.pages.contacts.repository.ContactRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactViewmodel(private val contactRepository: ContactRepository): ViewModel() {

    var user_id : String? = null

    var ctx : Context? = null

    var friends_content_view : RecyclerView? = null

    private val disposables = CompositeDisposable()

    var mLinearLayoutManager : LinearLayoutManager? = null

    var activity : Activity? = null

    fun getFollowers(){
        val disposable = contactRepository.getFollowers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({followers ->
                friends_content_view.also {
                    it?.layoutManager = LinearLayoutManager(ctx!!)
                    it?.setHasFixedSize(true)
                    it?.adapter = FriendsRecyclerAdapter(followers, activity)

                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }
}