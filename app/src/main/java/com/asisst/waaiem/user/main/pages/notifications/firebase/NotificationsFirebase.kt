package com.asisst.waaiem.user.main.pages.notifications.firebase

import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.asisst.waaiem.user.main.pages.pojo.Notifications
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable

class NotificationsFirebase {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun currentUser() = firebaseAuth.currentUser

    fun getNotifications() = Observable.create<MutableList<Notifications>> {

        var  interactions_list : MutableList<Notifications> = mutableListOf()

        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseDatabase.keepSynced(true)
        val query = firebaseDatabase.child("Notifications").child(currentUser()?.uid!!).orderByChild("timestamp")

        firebaseDatabase.child("Posts").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (dat in p0.children) {
                        val posts = dat.getValue(Posts::class.java)

                        query.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    interactions_list.clear()
                                    for (data in p0.children) {
                                        val interactions = data.getValue(Notifications::class.java)

                                        if (interactions?.type.equals("flame") && interactions?.post_id.equals(posts?.post_id)){
                                            interactions_list.add(interactions!!)
                                        }
                                        else if (interactions?.type.equals("spice") && interactions?.post_id.equals(posts?.post_id)){
                                            interactions_list.add(interactions!!)
                                        }else{}
                                        /*if (interactions?.type.equals("flame")){
                                            interactions_list.add(interactions!!)
                                        }*/
                                        val n_list = interactions_list.distinct().toMutableList()
                                        it.onNext(n_list)
                                    }
                                }
                            }
                        })

                    }
                }
            }
        })

    }
}