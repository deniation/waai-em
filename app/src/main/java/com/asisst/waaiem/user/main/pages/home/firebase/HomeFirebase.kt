package com.asisst.waaiem.user.main.pages.home.firebase

import android.net.Uri
import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.*

class HomeFirebase {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun currentUser() = firebaseAuth.currentUser

    fun getProfile() = Observable.create<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details")
            .child(currentUser()?.uid!!)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val logo_pic = p0.child("profile_picture").value.toString()
                    it.onNext(logo_pic)
                }else{
                }
            }

        })
    }

    fun getClickedUserProfile(user_id : String) = Observable.create<MutableList<String>> {
        var user_list : MutableList<String> = mutableListOf()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details").child(user_id)
        firebaseDatabase.keepSynced(true)
        firebaseDatabase.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val logo_pic = p0.child("profile_picture").value.toString()
                    val username = p0.child("username").value.toString()
                    val location = p0.child("location").value.toString()
                    user_list.add(logo_pic)
                    user_list.add(username)
                    user_list.add(location)
                    it.onNext(user_list)
                }else{
                }
            }

        })
    }

    fun post(photo: Uri, caption: String) = Completable.create { emitter ->
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Posts")
        val post_id = firebaseDatabase.push().key
        val timestamp = ServerValue.TIMESTAMP

        if (photo.toString().equals("no_image")){
            val post_map = mapOf(
                "content" to "no_image",
                "caption" to caption,
                "user_id" to currentUser()?.uid!!,
                "post_id" to post_id,
                "timestamp" to timestamp)
            firebaseDatabase.child(post_id!!).setValue(post_map).addOnCompleteListener {
                if (it.isSuccessful){
                    emitter.onComplete()
                }else{
                    emitter.onError(it.exception!!)
                }
            }
        }else{
            val firebaseStorage =  FirebaseStorage.getInstance().reference.child("Posts").child("Content")
            val post_picture_random_name = UUID.randomUUID().toString()
            val post_picture_filepath = firebaseStorage.child(post_picture_random_name + ".jpg")
            val uploadTask = post_picture_filepath.putFile(photo)
            uploadTask.continueWithTask {
                if (!emitter.isDisposed) {
                    if (!it.isSuccessful) {
                        emitter.onError(it.exception!!)
                    }
                }
                post_picture_filepath.downloadUrl
            }.addOnCompleteListener {
                if (!emitter.isDisposed) {
                    if (it.isSuccessful) {
                        val post_picture_download_url = it.result.toString()
                        val post_map = mapOf(
                            "content" to post_picture_download_url,
                            "caption" to caption,
                            "user_id" to currentUser()?.uid!!,
                            "post_id" to post_id,
                            "timestamp" to timestamp)
                        firebaseDatabase.child(post_id!!).setValue(post_map).addOnCompleteListener {
                            if (it.isSuccessful){
                                emitter.onComplete()
                            }else{
                                emitter.onError(it.exception!!)
                            }
                        }
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
            }
        }
    }

    fun getPosts() = Observable.create<MutableList<Posts>> {
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
                        posts_list.add(posts!!)
                        it.onNext(posts_list)
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

    fun getOthersTotalFollowers(user_id: String) = Observable.create<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Followers").child(user_id)
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

    fun getOthersTotalFollowings(user_id: String) = Observable.create<String> {
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Followings").child(user_id)
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
}