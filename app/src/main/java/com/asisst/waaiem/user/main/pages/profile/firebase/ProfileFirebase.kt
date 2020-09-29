package com.asisst.waaiem.user.main.pages.profile.firebase

import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.asisst.waaiem.user.main.pages.profile.pojo.Comments
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable

class ProfileFirebase{

private val firebaseAuth: FirebaseAuth by lazy {
    FirebaseAuth.getInstance()
}

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun getUserProfile() = Observable.create<MutableList<String>> {
        var user_list : MutableList<String> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details").child(currentUser()?.uid!!)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val logo_pic = p0.child("profile_picture").value.toString()
                    val username = p0.child("username").value.toString()
                    val firstname = p0.child("firstname").value.toString()
                    val lastname = p0.child("lastname").value.toString()
                    val location = p0.child("location").value.toString()
                    user_list.add(logo_pic)
                    user_list.add(username)
                    user_list.add(location)
                    user_list.add(firstname)
                    user_list.add(lastname)
                    it.onNext(user_list)
                }else{
                }
            }

        })
    }

    fun getTotalFollowers() = Observable.create<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Followers").child(currentUser()?.uid!!)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val total = p0.childrenCount.toString()
                    it.onNext(total)
                }else{
                    it.onNext("0")
                }
            }
        })

    }

    fun getTotalFollowings() = Observable.create<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Followings").child(currentUser()?.uid!!)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val total = p0.childrenCount.toString()
                    it.onNext(total)
                }else{
                    it.onNext("0")
                }
            }
        })

    }

    fun getInMyKitchen() = Observable.create<MutableList<Posts>> {
        var  posts_list : MutableList<Posts> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Posts")
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    posts_list.clear()
                    for (data in p0.children) {
                        val posts = data.getValue(Posts::class.java)
                        if (posts?.user_id.equals(currentUser()?.uid!!)) {
                            posts_list.add(posts!!)
                            it.onNext(posts_list)
                        }else{
                        }
                    }
                }
            }
        })

    }

    fun getComments(postId : String) = Observable.create<MutableList<Comments>> {
        val comments_list : MutableList<Comments> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.child("Comments").child(postId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                it.onError(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    //comments_list.clear()
                    for (data in p0.children) {
                        val comments = data.getValue(Comments::class.java)
                        comments_list.add(comments!!)
                        it.onNext(comments_list)
                    }

                }else{}
            }
        })

    }
}