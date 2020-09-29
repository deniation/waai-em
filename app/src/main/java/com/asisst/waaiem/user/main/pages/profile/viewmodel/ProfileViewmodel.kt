package com.asisst.waaiem.user.main.pages.profile.viewmodel

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.asisst.waaiem.R
import com.asisst.waaiem.user.main.pages.home.adapter.PostsRecyclerAdapter
import com.asisst.waaiem.user.main.pages.profile.adapter.CommentsRecyclerAdapter
import com.asisst.waaiem.user.main.pages.profile.adapter.InvitedKitchenRecyclerAdapter
import com.asisst.waaiem.user.main.pages.profile.repository.ProfileRepository
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ProfileViewmodel(private val profileRepository: ProfileRepository) : ViewModel() {

    var ctx : Context? = null

    var postId : String? = null
    var firstname: String? = null
    var lastname: String? = null
    var username: String? = null
    var phone_number : String? = null
    var bio : String? = null

    var profile_pic : CircleImageView? = null
    var edit_profile_pic : CircleImageView? = null

    var edit_profile_layout : ConstraintLayout? = null
    var blur: ConstraintLayout? = null

    var in_my_kitchen_content_view : RecyclerView? = null
    var comments_content_view : RecyclerView? = null

    var username_profile : TextView? = null
    var fullname_profile : TextView? = null
    var username_location : TextView? = null
    var t_follower : TextView? = null
    var t_followings : TextView? = null
    var edit_username_profile : EditText? = null
    var edit_firstname_profile : EditText? = null
    var edit_lastname_profile : EditText? = null
    var edit_phone_profile : EditText? = null
    var edit_bio_profile : EditText? = null

    var loading_btn : CircularProgressButton? = null

    private val disposables = CompositeDisposable()

    var mLinearLayoutManager : LinearLayoutManager? = null

    var activity : Activity? = null

    val user by lazy {
        profileRepository.getCurrentUser()?.uid
    }

    val logout by lazy {
        profileRepository.getLogOut()
    }

    fun getUserProfile(){
        val disposable = profileRepository.getUserProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                Glide.with(ctx!!).load(it[0]).placeholder(R.drawable.attach).into(profile_pic!!)
                Glide.with(ctx!!).load(it[0]).placeholder(R.drawable.attach).into(edit_profile_pic!!)
                username_profile?.text = "@" + it[1]
                username_location?.text = it[2]
                fullname_profile?.text = it[3] + " " + it[4]
                getTotalFollowers()
                getTotalFollowings()

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getTotalFollowers(){
        val disposable = profileRepository.getTotalFollowers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({total_followers ->
                t_follower?.text = total_followers
            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getTotalFollowings(){
        val disposable = profileRepository.getTotalFollowings()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({total_followings ->
                t_followings?.text = total_followings

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun openEditProfilePanel(){
        if (edit_profile_layout?.visibility == View.GONE){
            val animation = AnimationUtils.loadAnimation(ctx!!, R.anim.fade_in)
            edit_profile_layout?.animation = animation
            edit_profile_layout?.visibility = View.VISIBLE
        }
    }

    fun closeEditProfilePanel(){
        if (edit_profile_layout?.visibility == View.VISIBLE){
            val animation = AnimationUtils.loadAnimation(ctx!!, R.anim.fade_out)
            edit_profile_layout?.animation = animation
            edit_profile_layout?.visibility = View.GONE
        }
    }

    fun getInMyKitchen(){
        val disposable = profileRepository.getInMyKitchen()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({posts ->
                in_my_kitchen_content_view.also {
                    mLinearLayoutManager = LinearLayoutManager(ctx!!)
                    mLinearLayoutManager!!.reverseLayout = true
                    mLinearLayoutManager!!.stackFromEnd = true
                    it?.layoutManager = mLinearLayoutManager
                    it?.setHasFixedSize(true)
                    it?.adapter = InvitedKitchenRecyclerAdapter(posts, user, activity)
                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getComments(){
        val disposable = profileRepository.getComments(postId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({posts ->
                comments_content_view.also {
                    mLinearLayoutManager = LinearLayoutManager(ctx!!)
                    mLinearLayoutManager!!.reverseLayout = true
                    mLinearLayoutManager!!.stackFromEnd = true
                    it?.layoutManager = mLinearLayoutManager
                    it?.setHasFixedSize(true)
                    it?.adapter = CommentsRecyclerAdapter(posts)
                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

}