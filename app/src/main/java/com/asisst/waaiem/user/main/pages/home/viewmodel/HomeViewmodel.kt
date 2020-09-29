package com.asisst.waaiem.user.main.pages.home.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.asisst.waaiem.MainActivity
import com.asisst.waaiem.R
import com.asisst.waaiem.user.authentication.authinterface.AuthListener
import com.asisst.waaiem.user.main.pages.home.adapter.PostsRecyclerAdapter
import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.asisst.waaiem.user.main.pages.home.repository.HomeRepository
import com.asisst.waaiem.user.main.pages.profile.repository.ProfileRepository
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewmodel(private val homeRepository: HomeRepository, private val profileRepository: ProfileRepository): ViewModel() {

    var caption : String? = null
    var user_id : String? = null

    var profile_picture : Uri? = null

    var ctx : Context? = null

    var post_panel : ConstraintLayout? = null
    var blur: ConstraintLayout? = null
    var all_options : ConstraintLayout? = null
    var others_options: ConstraintLayout? = null
    var self_options : ConstraintLayout? = null

    var close_others_options: ImageView? = null
    var close_self_options : ImageView? = null
    var follow: ImageView? = null
    var unfollow : ImageView? = null
    var delete: ImageView? = null
    var block : ImageView? = null

    var profile_pic : CircleImageView? = null

    var username_profile : TextView? = null
    var username_location : TextView? = null
    var t_follower : TextView? = null
    var t_followings : TextView? = null
    var follow_count: TextView? = null
    var unfollow_count: TextView? = null

    var f_btn : Button? = null

    var social_content_view : RecyclerView? = null

    var search : SearchView? = null

    var post_loading_btn : CircularProgressButton? = null

    var authListener: AuthListener? = null

    var mLinearLayoutManager : LinearLayoutManager? = null

    var activity : Activity? = null

    private val disposables = CompositeDisposable()

    val user by lazy {
        homeRepository.currentUser()?.uid
    }

    fun openPostPanel(view : View){
        if (post_panel?.visibility == View.GONE){
            val animation = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
            post_panel?.animation = animation
            post_panel?.visibility = View.VISIBLE
            getProfile()
        }else{
            val animation = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
            post_panel?.animation = animation
            post_panel?.visibility = View.GONE
        }
    }

    fun closeContent(view : View){
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
        post_panel?.animation = animation
        post_panel?.visibility = View.GONE
    }

    fun postContent(view : View){
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.fade_out)
        post_panel?.animation = animation
        post_panel?.visibility = View.GONE

        post_loading_btn?.startAnimation()
        if (profile_picture == null && caption.isNullOrEmpty()){
            Toast.makeText(view.context, "Please add content", Toast.LENGTH_LONG).show()
            post_loading_btn?.revertAnimation()
            return
        }
        if (profile_picture == null && !caption.isNullOrEmpty()){
            profile_picture = Uri.parse("no_image")
        }
        if (profile_picture != null && caption.isNullOrEmpty()){
            caption = "no_caption"
        }
        val disposable = homeRepository.post(profile_picture!!, caption!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(view.context, "Posted Successful", Toast.LENGTH_LONG).show()
                val bp = BitmapFactory.decodeResource(view.context.resources, R.drawable.logo)
                val intent = Intent(view.context, MainActivity::class.java)
                view.context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                if (bp != null) {
                    post_loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), bp)
                }
            }, {
                post_loading_btn?.revertAnimation()
                Toast.makeText(view.context, it.message!!, Toast.LENGTH_LONG).show()
            })
        disposables.add(disposable)
    }

    fun getProfile(){
        val disposable = profileRepository.getProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                Glide.with(ctx!!).load(it).placeholder(R.drawable.attach).into(profile_pic!!)
            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getClickedUserProfile(){
        val disposable = profileRepository.getClickedUserProfile(user_id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                Glide.with(ctx!!).load(it[0]).placeholder(R.drawable.attach).into(profile_pic!!)
                username_profile?.text = it[1]
                username_location?.text = it[2]
                getOthersTotalFollowers()
                getOthersTotalFollowings()
                getFollowingState()

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getFollowingState(){
        val disposable = homeRepository.getFollowingState(user_id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({f_state ->
                if (f_state.equals("Following")){
                    f_btn?.text = "Unfollow"
                }else{

                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getOthersTotalFollowers(){
        val disposable = homeRepository.getOthersTotalFollowers(user_id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({total_followers ->
                t_follower?.text = total_followers
            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getOthersTotalFollowings(){
        val disposable = homeRepository.getOthersTotalFollowings(user_id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({total_followings ->
                t_followings?.text = total_followings
            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
    }

    fun getPosts() : Int{
        val disposable = homeRepository.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({posts ->
                social_content_view.also {
                    mLinearLayoutManager = LinearLayoutManager(ctx!!)
                    mLinearLayoutManager!!.reverseLayout = true
                    mLinearLayoutManager!!.stackFromEnd = true
                    it?.layoutManager = mLinearLayoutManager
                    it?.setHasFixedSize(true)
                    it?.adapter = PostsRecyclerAdapter(posts, user, activity, all_options, others_options, self_options, close_others_options,
                        close_self_options, follow, unfollow, follow_count, unfollow_count, delete)

                }

                if (search != null){
                    search!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            val search_list : MutableList<Posts> = ArrayList()
                            for (post in posts){
                                if (post.caption.toLowerCase().contains(newText!!, ignoreCase = true)){
                                    search_list.add(post)
                                }
                            }
                            social_content_view.also {
                                mLinearLayoutManager = LinearLayoutManager(ctx!!)
                                mLinearLayoutManager!!.reverseLayout = true
                                mLinearLayoutManager!!.stackFromEnd = true
                                it?.layoutManager = mLinearLayoutManager
                                it?.setHasFixedSize(true)
                                it?.adapter = PostsRecyclerAdapter(
                                    search_list,
                                    user,
                                    activity,
                                    all_options,
                                    others_options,
                                    self_options,
                                    close_others_options,
                                    close_self_options,
                                    follow,
                                    unfollow,
                                    follow_count,
                                    unfollow_count,
                                    delete
                                )

                            }
                            return true
                        }

                    })
                }

            }, {
                //sending a failure callback

            })
        disposables.add(disposable)
        return 0
    }

}