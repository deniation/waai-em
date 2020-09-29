package com.asisst.waaiem.user.main.pages.contacts.firebase

import com.asisst.waaiem.user.main.pages.contacts.pojo.Contacts
import com.asisst.waaiem.user.main.pages.contacts.pojo.Followers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable

class ContactFirebase {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun currentUser() = firebaseAuth.currentUser

    fun getContacts() = Observable.create<MutableList<Contacts>> {
        var  contacts_list : MutableList<Contacts> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details")
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    contacts_list.clear()
                    for (data in p0.children) {
                        val contacts = data.getValue(Contacts::class.java)
                        if (!contacts?.user_id.equals(currentUser()?.uid!!)) {
                            contacts_list.add(contacts!!)
                            it.onNext(contacts_list)
                        }else{
                        }
                    }
                }
            }
        })

    }

    fun getFollowingState(user_id: String) = Observable.create<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Followings").child(currentUser()?.uid!!).child(user_id)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    it.onNext("Following")
                }else{
                    it.onNext("Not_Following")
                }
            }
        })

    }

    fun getFollowers() = Observable.create<MutableList<Followers>> {
        var  followers_list : MutableList<Followers> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Followings").child(currentUser()?.uid!!)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    followers_list.clear()
                    for (data in p0.children) {
                        val followers = data.getValue(Followers::class.java)
                        followers_list.add(followers!!)
                        it.onNext(followers_list)
                    }
                }
            }
        })

    }
}