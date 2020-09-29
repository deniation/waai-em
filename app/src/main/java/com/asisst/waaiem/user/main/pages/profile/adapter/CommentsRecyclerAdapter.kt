package com.asisst.waaiem.user.main.pages.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.CommentsViewBinding
import com.asisst.waaiem.user.main.pages.profile.pojo.Comments
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.database.*

class CommentsRecyclerAdapter(private val comments_list: List<Comments>) : RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsViewHolder>() {

    private var ctx : Context? = null
    var firebaseDatabase : DatabaseReference? = null

    inner class CommentsViewHolder(val commentViewBinding: CommentsViewBinding) : RecyclerView.ViewHolder(commentViewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        ctx = parent.context
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseDatabase!!.keepSynced(true)

        return CommentsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.comments_view, parent, false
            ))    }

    override fun getItemCount(): Int {
        return comments_list.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment_user = comments_list[position].user_id
        val comment_content = comments_list[position].comment
        val commented_post = comments_list[position].post_id
        val timeAgo = TimeAgo.using(comments_list.get(position).timestamp)

        holder.commentViewBinding.textView19.text = timeAgo
        holder.commentViewBinding.textView20.text = comment_content

        firebaseDatabase?.child("Details")?.child(comment_user)?.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val profile = p0.child("profile_picture").value.toString()
                    val name = p0.child("username").value.toString()
                    val fname = p0.child("firstname").value.toString()
                    val lname = p0.child("lastname").value.toString()
                    Glide.with(ctx!!).load(profile).into(holder.commentViewBinding.postsProfPicId)
                    holder.commentViewBinding.textView17.text = "$fname $lname"
                    holder.commentViewBinding.textView18.text = "@"+name
                }
            }

        })
    }

}