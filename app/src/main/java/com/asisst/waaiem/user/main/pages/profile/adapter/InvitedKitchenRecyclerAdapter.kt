package com.asisst.waaiem.user.main.pages.profile.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.PostsViewBinding
import com.asisst.waaiem.user.authentication.pages.LoginActivity
import com.asisst.waaiem.user.main.pages.home.page.CommentsActivity
import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.asisst.waaiem.user.main.pages.profile.pojo.Comments
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.database.*

class InvitedKitchenRecyclerAdapter(
    private val posts: List<Posts>,
    private val user: String?,
    private val activity: Activity?
) : RecyclerView.Adapter<InvitedKitchenRecyclerAdapter.PostsViewHolder>() {

    var firebaseDatabase : DatabaseReference? = null
    var fDatabase : DatabaseReference? = null

    private var ctx : Context? = null

    inner class PostsViewHolder(val postsViewBinding: PostsViewBinding) : RecyclerView.ViewHolder(postsViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {

        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        firebaseDatabase!!.keepSynced(true)

        fDatabase = FirebaseDatabase.getInstance().reference

        fDatabase!!.keepSynced(true)

        ctx = parent.context

        return PostsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.posts_view, parent, false
            ))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val timeAgo = TimeAgo.using(posts.get(position).timestamp)
        val caption = posts.get(position).caption
        val content = posts.get(position).content
        val user_id = posts.get(position).user_id
        var comment_pic = ""
        var comment_uid = ""
        var comment_fullname = ""
        var comment_flames = ""
        var comment_spice = ""
        var comment_stale = ""

        val pos = holder.adapterPosition
        val postId = posts.get(pos).post_id

        val post_owner = posts.get(pos).user_id

        val hashtag = caption.substringAfter("#").substringBefore(" ")
        val atname = caption.substringAfter("@").substringBefore(" ")
        val link = caption.substringAfter("www.").substringBefore(" ")

        holder.postsViewBinding.postsTimeId.text = timeAgo

        if (caption.equals("no_caption")) {
            holder.postsViewBinding.postsCaptionId.visibility = View.GONE
        }
        else{
            if (caption.contains("#", ignoreCase = true)){
                val end = hashtag.length + 1
                val start = caption.indexOf("#")


                val spannableString = SpannableString(caption)
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#03A9F4"))
                if (start > -1){
                    spannableString.setSpan(foregroundColorSpan, start, start+end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    holder.postsViewBinding.postsCaptionId.text = spannableString
                }

            }
            else if (caption.contains("@", ignoreCase = true)){
                val end = atname.length + 1
                val start = caption.indexOf("@")


                val spannableString = SpannableString(caption)
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#03A9F4"))
                if (start > -1){
                    spannableString.setSpan(foregroundColorSpan, start, start+end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    holder.postsViewBinding.postsCaptionId.text = spannableString
                }
            }
            else if (caption.contains("www.", ignoreCase = true)){
                val end = link.length + 4
                val start = caption.indexOf("www.")


                val spannableString = SpannableString(caption)
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#03A9F4"))
                if (start > -1){
                    spannableString.setSpan(foregroundColorSpan, start, start+end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    holder.postsViewBinding.postsCaptionId.text = spannableString
                }
            }
            else {
                holder.postsViewBinding.postsCaptionId.text = caption
            }

        }

        if (content.equals("no_image")){
            holder.postsViewBinding.postsImageViewId.visibility = View.GONE
        }
        else{
            holder.postsViewBinding.postsImageViewId.visibility = View.VISIBLE
            Glide.with(ctx!!).load(content).into(holder.postsViewBinding.postsImageViewId)
        }

        firebaseDatabase?.child("Details")?.child(user_id)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val name = p0.child("username").value.toString()
                    val fname = p0.child("firstname").value.toString()
                    val lname = p0.child("lastname").value.toString()
                    val prof_pic = p0.child("profile_picture").value.toString()

                    comment_pic = prof_pic
                    comment_uid = name
                    comment_fullname = "$fname $lname"

                    Glide.with(ctx!!).load(prof_pic).into(holder.postsViewBinding.postsProfPicId)
                    holder.postsViewBinding.postsUsernameId.text = "$fname $lname"
                    holder.postsViewBinding.postsNameId.text = "@$name"
                }
            }

        })

        firebaseDatabase?.child("Comments")?.child(postId)?.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var total_flames : String? = "null"
                if (p0.exists()){
                    total_flames = p0.childrenCount.toString()
                }else{
                    total_flames = "0"
                }
                holder.postsViewBinding.postsCommentsCount.text = total_flames
            }

        })

        firebaseDatabase?.child("Comments")?.child(postId)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (data in p0.children) {
                        val comments = data.getValue(Comments::class.java)
                        if (comments?.user_id.equals(user)){
                            holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_comments)
                        }
                        else{
                            holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_comments_unselected)
                        }
                    }

                }else{
                    holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_comments_unselected)
                }
            }
        })

        firebaseDatabase?.child("Flames")?.child(postId)?.child(user!!)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_flames_selected)
                }else{
                    holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_flame)
                }
            }
        })

        firebaseDatabase?.child("Flames")?.child(postId)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var total_flames : String? = "null"
                if (p0.exists()) {
                    total_flames = p0.childrenCount.toString()
                }else{
                    total_flames = "0"
                }
                holder.postsViewBinding.postsFlameCount.text = total_flames
                comment_flames = total_flames as String
            }
        })

        firebaseDatabase?.child("Spices")?.child(postId)?.child(user!!)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    holder.postsViewBinding.postsSpiceId.setImageResource(R.drawable.ic_spice_selected)
                }else{
                    holder.postsViewBinding.postsSpiceId.setImageResource(R.drawable.ic_spice_unselected)
                }
            }
        })

        firebaseDatabase?.child("Spices")?.child(postId)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var total_flames : String? = "null"
                if (p0.exists()) {
                    total_flames = p0.childrenCount.toString()
                }else{
                    total_flames = "0"
                }
                comment_spice = total_flames as String
                holder.postsViewBinding.postsSpiceCount.text = total_flames
            }
        })

        firebaseDatabase?.child("Stales")?.child(postId)?.child(user!!)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    holder.postsViewBinding.postsStaleId.setImageResource(R.drawable.ic_stale_selected)
                }else{
                    holder.postsViewBinding.postsStaleId.setImageResource(R.drawable.ic_stale_unselected)
                }
            }
        })

        firebaseDatabase?.child("Stales")?.child(postId)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var total_flames : String? = "null"
                if (p0.exists()) {
                    total_flames = p0.childrenCount.toString()
                }else{
                    total_flames = "0"
                }
                comment_stale = total_flames as String
                holder.postsViewBinding.postsStaleCount.text = total_flames
            }
        })

        //_________________________________________________CLICKS________________________________________________________________
        holder.postsViewBinding.postsFlameId.setOnClickListener {
            val timestamp = ServerValue.TIMESTAMP
            val flame_map = mapOf(
                "user_id" to user,
                "post_owner_id" to post_owner,
                "post_id" to postId,
                "type" to "flame",
                "seen" to false,
                "timestamp" to timestamp)

            firebaseDatabase?.child("Flames")?.child(postId)?.child(user!!)?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        firebaseDatabase?.child("Flames")?.child(postId)?.child(user!!)?.removeValue()?.addOnCompleteListener {
                            if (it.isSuccessful){
                                holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_flame)
                                Toast.makeText(ctx!!, "Unflamed", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        firebaseDatabase?.child("Flames")?.child(postId!!)?.child(user!!)?.setValue(flame_map)?.addOnCompleteListener {
                            if (it.isSuccessful){
                                firebaseDatabase?.child("Notifications")?.child(post_owner!!)?.push()?.setValue(flame_map)?.addOnCompleteListener {
                                    if (it.isSuccessful){
                                        holder.postsViewBinding.postsFlameId.setImageResource(R.drawable.ic_flames_selected)
                                        Toast.makeText(ctx!!, "Flamed", Toast.LENGTH_LONG).show()
                                    }else{
                                        Toast.makeText(ctx!!, "Failed to flame", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }else{
                                Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            })
        }

        holder.postsViewBinding.postsSpiceId.setOnClickListener {
            val timestamp = ServerValue.TIMESTAMP
            val spice_map = mapOf(
                "user_id" to user,
                "post_owner_id" to post_owner,
                "post_id" to postId,
                "type" to "spice",
                "seen" to false,
                "timestamp" to timestamp)

            firebaseDatabase?.child("Spices")?.child(postId)?.child(user!!)?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        firebaseDatabase?.child("Spices")?.child(postId)?.child(user!!)?.removeValue()?.addOnCompleteListener {
                            if (it.isSuccessful){
                                holder.postsViewBinding.postsSpiceId.setImageResource(R.drawable.ic_spice_unselected)
                                Toast.makeText(ctx!!, "Unspiced", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        firebaseDatabase?.child("Spices")?.child(postId!!)?.child(user!!)?.setValue(spice_map)?.addOnCompleteListener {
                            if (it.isSuccessful){
                                firebaseDatabase?.child("Notifications")?.child(post_owner!!)?.push()?.setValue(spice_map)?.addOnCompleteListener {
                                    if (it.isSuccessful){
                                        holder.postsViewBinding.postsSpiceId.setImageResource(R.drawable.ic_spice_selected)
                                        Toast.makeText(ctx!!, "Spiced", Toast.LENGTH_LONG).show()
                                    }else{
                                        Toast.makeText(ctx!!, "Failed to spice", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }else{
                                Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            })
        }

        holder.postsViewBinding.postsStaleId.setOnClickListener {
            val timestamp = ServerValue.TIMESTAMP
            val stale_map = mapOf(
                "user_id" to user,
                "post_owner_id" to post_owner,
                "post_id" to postId,
                "type" to "stale",
                "timestamp" to timestamp)

            firebaseDatabase?.child("Stales")?.child(postId)?.child(user!!)?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        firebaseDatabase?.child("Stales")?.child(postId)?.child(user!!)?.removeValue()?.addOnCompleteListener {
                            if (it.isSuccessful){
                                holder.postsViewBinding.postsStaleId.setImageResource(R.drawable.ic_stale_unselected)
                                Toast.makeText(ctx!!, "Unstaled", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        firebaseDatabase?.child("Stales")?.child(postId!!)?.child(user!!)?.setValue(stale_map)?.addOnCompleteListener {
                            if (it.isSuccessful){
                                holder.postsViewBinding.postsStaleId.setImageResource(R.drawable.ic_stale_selected)
                                Toast.makeText(ctx!!, "Staled", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            })
        }

        /*holder.postsViewBinding.postsMoreId.setOnClickListener {
            if (post_owner.equals(user!!)){
                if (holder.postsViewBinding.postsMLayout.visibility == View.VISIBLE){
                    val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
                    holder.postsViewBinding.postsMLayout.animation = animation
                    //blur.visibility = View.GONE
                    holder.postsViewBinding.postsMLayout.visibility = View.GONE
                }else{
                    val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in)
                    holder.postsViewBinding.postsMLayout.animation = animation
                    holder.postsViewBinding.postsMLayout.visibility = View.VISIBLE
                    //blur.visibility = View.VISIBLE
                }
            }
            else{

                if (holder.postsViewBinding.postsMoreLayout.visibility == View.VISIBLE){
                    val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
                    holder.postsViewBinding.postsMoreLayout.animation = animation
                    holder.postsViewBinding.postsMoreLayout.visibility = View.GONE
                }else{

                    val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in)
                    holder.postsViewBinding.postsMoreLayout.animation = animation
                    holder.postsViewBinding.postsMoreLayout.visibility = View.VISIBLE

                    firebaseDatabase?.child("Followings")?.child(user)?.child(post_owner)?.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()){
                                holder.postsViewBinding.imageView42.visibility = View.GONE
                                holder.postsViewBinding.textView140.visibility = View.GONE
                                holder.postsViewBinding.imageView48.visibility = View.VISIBLE
                                holder.postsViewBinding.textView141.visibility = View.VISIBLE
                            }else{
                                holder.postsViewBinding.imageView42.visibility = View.VISIBLE
                                holder.postsViewBinding.textView140.visibility = View.VISIBLE
                                holder.postsViewBinding.imageView48.visibility = View.GONE
                                holder.postsViewBinding.textView141.visibility = View.GONE
                            }
                        }

                    })

                }

            }
        }

        holder.postsViewBinding.imageView37.setOnClickListener {
            firebaseDatabase?.child("Posts")?.child(postId)?.removeValue()?.addOnCompleteListener {
                if (it.isSuccessful){
                    firebaseDatabase?.child("Comments")?.child(postId)?.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()){
                                firebaseDatabase?.child("Comments")?.child(postId)?.removeValue()
                            }else{}
                        }

                    })
                    Toast.makeText(ctx, "Post Deleted", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(ctx, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        holder.postsViewBinding.postsProfPicId.setOnClickListener {
            if (!user.equals(post_owner)){
            }else{}
        }

        holder.postsViewBinding.imageView42.setOnClickListener {

            val timestamp = ServerValue.TIMESTAMP
            val follow_map = mapOf(
                "user_id" to user,
                "post_owner_id" to post_owner,
                "type" to "follow",
                "seen" to false,
                "timestamp" to timestamp)

            val c_msg_uid = "Users/" + "Followers/" + post_owner + "/" + user!!
            val r_msg_uid = "Users/" + "Followings/"  + user!! + "/" + post_owner

            val f_map = HashMap<String, Any>()
            f_map.put(c_msg_uid, follow_map)
            f_map.put(r_msg_uid, follow_map)

            fDatabase?.updateChildren(f_map, DatabaseReference.CompletionListener { databaseError, databaseReference ->
                if (databaseError?.message != null) {
                    Toast.makeText(ctx!!, databaseError?.message.toString(), Toast.LENGTH_LONG).show()

                } else {
                    firebaseDatabase?.child("Notifications")?.child(post_owner!!)?.push()?.setValue(follow_map)?.addOnCompleteListener {
                        if (it.isSuccessful){
                            holder.postsViewBinding.imageView42.visibility = View.GONE
                            holder.postsViewBinding.textView140.visibility = View.GONE
                            holder.postsViewBinding.imageView48.visibility = View.VISIBLE
                            holder.postsViewBinding.textView141.visibility = View.VISIBLE
                            Toast.makeText(ctx!!, "Followed", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(ctx!!, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })

            /*firebaseDatabase?.child("Followers")?.child(current_user_id!!)?.child(post_owner)?.setValue(follow_map)?.addOnCompleteListener {
                if (it.isSuccessful){

                }else{
                    Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }*/

        }

        holder.postsViewBinding.imageView48.setOnClickListener {

            firebaseDatabase?.child("Followers")?.child(user!!)?.child(post_owner)?.removeValue()?.addOnCompleteListener {
                if (it.isSuccessful){
                    holder.postsViewBinding.imageView42.visibility = View.VISIBLE
                    holder.postsViewBinding.textView140.visibility = View.VISIBLE
                    holder.postsViewBinding.imageView48.visibility = View.GONE
                    holder.postsViewBinding.textView141.visibility = View.GONE
                    Toast.makeText(ctx!!, "Unfollowed", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(ctx!!, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }

        }

        holder.postsViewBinding.report.setOnClickListener {
            if (holder.postsViewBinding.reportCatId.visibility == View.VISIBLE){
                val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
                holder.postsViewBinding.reportCatId.animation = animation
                holder.postsViewBinding.reportCatId.visibility = View.GONE
            }else{
                val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in)
                holder.postsViewBinding.reportCatId.animation = animation
                holder.postsViewBinding.reportCatId.visibility = View.VISIBLE
            }
        }

        holder.postsViewBinding.textView170.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
            holder.postsViewBinding.reportCatId.animation = animation
            holder.postsViewBinding.reportCatId.visibility = View.GONE
            Toast.makeText(ctx, "Post reported as HATE SPEECH", Toast.LENGTH_LONG).show()
        }

        holder.postsViewBinding.textView171.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
            holder.postsViewBinding.reportCatId.animation = animation
            holder.postsViewBinding.reportCatId.visibility = View.GONE
            Toast.makeText(ctx, "Post reported as TERRORISM", Toast.LENGTH_LONG).show()
        }

        holder.postsViewBinding.textView172.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
            holder.postsViewBinding.reportCatId.animation = animation
            holder.postsViewBinding.reportCatId.visibility = View.GONE
            Toast.makeText(ctx, "Post reported as a SPAM", Toast.LENGTH_LONG).show()
        }*/

        holder.postsViewBinding.postsCommentsId.setOnClickListener {
            val intent = Intent(activity, CommentsActivity::class.java)
            intent.putExtra("content", content)
            intent.putExtra("caption", caption)
            intent.putExtra("time_ago", timeAgo)
            intent.putExtra("uid", comment_uid)
            intent.putExtra("fname", comment_fullname)
            intent.putExtra("pic", comment_pic)
            intent.putExtra("flames", comment_flames)
            intent.putExtra("spice", comment_spice)
            intent.putExtra("stale", comment_stale)
            intent.putExtra("id", postId)
            intent.putExtra("user_id", user)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

    }
}