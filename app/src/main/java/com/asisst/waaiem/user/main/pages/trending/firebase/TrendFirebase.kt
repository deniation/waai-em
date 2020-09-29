package com.asisst.waaiem.user.main.pages.trending.firebase

import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable

class TrendFirebase {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun getHashtags() = Observable.create<MutableList<String>> {
        var  htag_list : MutableList<String> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Posts")
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    htag_list.clear()
                    for (data in p0.children) {
                        val hashtags = data.getValue(Posts::class.java)
                        val caption = hashtags?.caption
                        if (!caption.equals("no_caption")) {
                            if (caption!!.contains("#", ignoreCase = true)){
                                val hashtag = caption.substringAfter("#").substringBefore(" ")
                                if (caption.contains(hashtag, ignoreCase = true)){
                                    htag_list.add(hashtag)
                                }

                            }else{}
                        }else{}
                    }
                    val hashtag_list = htag_list.distinct().toMutableList()
                    it.onNext(hashtag_list)
                }
            }
        })

    }
}