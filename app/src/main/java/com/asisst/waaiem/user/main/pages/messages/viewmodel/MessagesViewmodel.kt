package com.asisst.waaiem.user.main.pages.messages.viewmodel

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asisst.metimesocial.ym.user.adapter.MessagesRecyclerAdapter
import com.asisst.waaiem.user.main.pages.messages.adapter.ChatsRecyclerAdapter
import com.asisst.waaiem.user.main.pages.messages.repository.MessagesRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MessagesViewmodel(private val messagesRepository: MessagesRepository) : ViewModel() {

    var message : String? = null
    var user_id : String? = null
    var username: String? = null

    var msg_content_view : RecyclerView? = null

    var message_area : EditText? = null

    private val disposables = CompositeDisposable()

    var mLinearLayoutManager : LinearLayoutManager? = null

    var activity : Activity? = null
    var ctx : Context? = null

    fun sendMessage(view : View){
        if (message.isNullOrEmpty()){
            return
        }
        if (user_id.isNullOrEmpty()){
            return
        }
        val disposable = messagesRepository.sendMessage(message!!, user_id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(view.context, "Posted Successful", Toast.LENGTH_LONG).show()
            }, {
            })
        message_area?.text = null
        disposables.add(disposable)
    }

    fun getMessages(){
        val disposable = messagesRepository.getConversation(user_id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({messages ->
                msg_content_view.also {
                    it?.layoutManager = LinearLayoutManager(ctx!!)
                    it?.setHasFixedSize(true)
                    it?.scrollToPosition(messages.size - 1)
                    it?.adapter = MessagesRecyclerAdapter(messages)

                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getChats(){
        val disposable = messagesRepository.getChats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({messages ->
                msg_content_view.also {
                    it?.layoutManager = LinearLayoutManager(ctx!!)
                    it?.setHasFixedSize(true)
                    it?.adapter = ChatsRecyclerAdapter(messages, activity)
                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }
}