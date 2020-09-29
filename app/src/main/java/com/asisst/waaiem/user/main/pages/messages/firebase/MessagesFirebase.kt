package com.asisst.waaiem.user.main.pages.messages.firebase

import com.asisst.waaiem.user.main.pages.messages.pojo.Chats
import com.asisst.waaiem.user.main.pages.messages.pojo.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class MessagesFirebase {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun currentUser() = firebaseAuth.currentUser

    fun sendMessage(message: String, recepient: String) = Completable.create { emitter ->
        val firebaseDatabase = FirebaseDatabase.getInstance().reference

        val c_chat = "Users/" + "Chats/" + currentUser()!!.uid + "/" + recepient
        val r_chat = "Users/" + "Chats/"  + recepient + "/" + currentUser()!!.uid

        val chat_map = HashMap<String, Any>()
        chat_map.put("seen", false)
        chat_map.put("timestamp", ServerValue.TIMESTAMP)
        chat_map.put("type", "text")
        chat_map.put("from", currentUser()!!.uid)
        chat_map.put("to", recepient)

        val chat = HashMap<String, Any>()
        chat.put(c_chat, chat_map)
        chat.put(r_chat, chat_map)

        firebaseDatabase.updateChildren(chat, DatabaseReference.CompletionListener { dbError, dbReference ->
                if (dbError?.message != null) {
                    emitter.onError(dbError.toException())

                } else {
                    val push_id = FirebaseDatabase.getInstance().reference.child("Users").child("Messages").child(currentUser()!!.uid).child(recepient).push().key

                    val m_map = HashMap<String, Any>()
                    m_map.put("seen", false)
                    m_map.put("timestamp", ServerValue.TIMESTAMP)
                    m_map.put("message", message)
                    m_map.put("type", "text")
                    m_map.put("from", currentUser()!!.uid)
                    m_map.put("to", recepient)

                    val c_msg_uid = "Users/" + "Messages/" + currentUser()!!.uid + "/" + recepient + "/" + push_id
                    val r_msg_uid = "Users/" + "Messages/"  + recepient + "/" + currentUser()!!.uid + "/" + push_id

                    val msg_map = HashMap<String, Any>()
                    msg_map.put(c_msg_uid, m_map)
                    msg_map.put(r_msg_uid, m_map)

                    firebaseDatabase.updateChildren(msg_map, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                        if (emitter.isDisposed) {
                            if (databaseError?.message != null) {
                                emitter.onError(databaseError.toException())

                            } else {
                                emitter.onComplete()
                            }
                        }
                    })
                }
        })
    }

    fun getConversation(recepient: String) = Observable.create<MutableList<Messages>> {
        var  messages_list : MutableList<Messages> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Messages")
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.child(currentUser()!!.uid).child(recepient).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                it.onError(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    messages_list.clear()
                    for (data in p0.children) {
                        val messages = data.getValue(Messages::class.java)
                        messages_list.add(messages!!)
                        it.onNext(messages_list)
                    }
                }else{}
            }
        })

    }

    fun getChats() = Observable.create<MutableList<Chats>> {
        var  chats_list : MutableList<Chats> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.child("Chats").child(currentUser()!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                it.onError(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    chats_list.clear()
                    for (data in p0.children) {
                        val chats = data.getValue(Chats::class.java)
                        chats_list.add(chats!!)
                        it.onNext(chats_list)
                    }

                }else{}
            }
        })

    }
}