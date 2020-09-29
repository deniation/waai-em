package com.asisst.waaiem.user.authentication.firebase

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.asisst.waaiem.util.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.*

class AuthFirebase {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState : LiveData<LoadingState>
        get() = _loadingState

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun login(email: String, password: String) = Observable.create<String> { emit ->
        //_loadingState.postValue(LoadingState.LOADING)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                val firebaseDatabase = FirebaseDatabase.getInstance().reference
                firebaseDatabase.child("Users").child("Details").child(currentUser()?.uid.toString()).addValueEventListener(object :
                    ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        emit.onError(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            emit.onNext("User")
                            //_loadingState.postValue(LoadingState.LOADED)
                        }else{
                            firebaseDatabase.child("Vendors").child("Details").child(currentUser()?.uid.toString()).addValueEventListener(object :
                                ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    emit.onError(p0.toException())
                                    //_loadingState.postValue(LoadingState.ERROR)
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()){
                                        emit.onNext("Vendor")
                                        //_loadingState.postValue(LoadingState.LOADED)
                                    }else{
                                        firebaseDatabase.child("Drivers").child("Details").child(currentUser()?.uid.toString()).addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {
                                                emit.onError(p0.toException())
                                                //_loadingState.postValue(LoadingState.ERROR)
                                            }

                                            override fun onDataChange(p0: DataSnapshot) {
                                                if (p0.exists()){
                                                    emit.onNext("Driver")
                                                    //_loadingState.postValue(LoadingState.LOADED)
                                                }else{
                                                   // _loadingState.postValue(LoadingState.ERROR)
                                                }
                                            }

                                        })
                                    }
                                }

                            })
                        }
                    }

                })
            }
            else{
                //_loadingState.postValue(LoadingState.ERROR)
            }
        }
    }

    fun forgotPassword(email: String) = Completable.create { emitter ->
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    emitter.onComplete()
                }
                else {
                    emitter.onError(it.exception!!)
                }
            }
        }

    }

    fun register(email: String, password: String, username: String, firstname: String, lastname: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            val user_id = firebaseAuth.currentUser?.uid.toString()
            val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details")
            val user_map = mapOf("username" to username, "firstname" to firstname, "lastname" to lastname)
            if (!emitter.isDisposed) {
                if (it.isSuccessful){
                    firebaseDatabase.child(user_id).setValue(user_map).addOnCompleteListener {
                        if (it.isSuccessful){
                            emitter.onComplete()
                        }else{
                            emitter.onError(it.exception!!)
                        }
                    }
                } else{
                    emitter.onError(it.exception!!)
                }

            }
        }
    }

    fun profileSetup(profile_picture: Uri, phone_number: String, recovery_email: String, location: String) = Completable.create { emitter ->
        val user_id = firebaseAuth.currentUser?.uid
        val firebaseStorage =  FirebaseStorage.getInstance().reference.child("Profile_Photos").child("Users")
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details")
        val profile_picture_random_name = UUID.randomUUID().toString()
        val profile_picture_filepath = firebaseStorage.child(profile_picture_random_name + ".jpg")
        val uploadTask = profile_picture_filepath.putFile(profile_picture)
        uploadTask.continueWithTask {
            if (!emitter.isDisposed) {
                if (!it.isSuccessful) {
                    emitter.onError(it.exception!!)
                }
            }
            profile_picture_filepath.downloadUrl
        }.addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    val profile_picture_download_url = it.result.toString()
                    val user_map = mapOf(
                        "recovery_email" to recovery_email,
                        "contact" to phone_number,
                        "location" to location,
                        "profile_picture" to profile_picture_download_url
                    )
                    firebaseDatabase.child(user_id!!).updateChildren(
                        user_map,
                        DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError?.message != null) {
                                //emitter.onError(databaseError.message)
                            } else {
                                emitter.onComplete()
                            }
                        })
                } else {
                    emitter.onError(it.exception!!)
                }
            }
        }
    }

    fun morePersonalInformation(gender: String, birthday_date: String, occupation: String, residence: String, relationship: String, education: String, religion: String, languages_spoken: String,
                                favourite_sport_team: String, favourite_color: String, favourite_book: String, favourite_movie: String, favourite_tv_show: String, favourite_music_genre: String, favourite_food: String,
                                favourite_drink: String, favourite_travel_destination: String) = Completable.create { emitter ->
        val user_id = firebaseAuth.currentUser?.uid
        val firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users").child("Details")
        val user_map = mapOf("gender" to gender, "birthday_date" to birthday_date, "occupation" to occupation, "residence" to residence, "relationship" to relationship, "education" to education,
            "religion" to religion, "languages_spoken" to languages_spoken, "favourite_sport_team" to favourite_sport_team, "favourite_color" to favourite_color, "favourite_book" to favourite_book,
            "favourite_movie" to favourite_movie, "favourite_tv_show" to favourite_tv_show, "favourite_music_genre" to favourite_music_genre, "favourite_food" to favourite_food,
            "favourite_drink" to favourite_drink, "favourite_travel_destination" to favourite_travel_destination)

        firebaseDatabase.child(user_id!!).updateChildren(
            user_map,
            DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if (!emitter.isDisposed) {
                    if (databaseError?.message != null) {
                        //emitter.onError(databaseError.message)
                    } else {
                        emitter.onComplete()
                    }
                }
            })

    }
}