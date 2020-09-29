package com.asisst.waaiem.user.main.pages.trending.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.SelectedTrendHolderBinding
import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.database.*

class SelectedTrendRecyclerAdapter(private val postsList: List<Posts>) : RecyclerView.Adapter<SelectedTrendRecyclerAdapter.SelectedTrendingViewHolder>() {

    private var ctx : Context? = null

    var fdb : DatabaseReference? = null

    inner class SelectedTrendingViewHolder(val selectedTrendingHolderBinding: SelectedTrendHolderBinding) : RecyclerView.ViewHolder(selectedTrendingHolderBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedTrendingViewHolder {

        ctx = parent.context

        fdb = FirebaseDatabase.getInstance().reference

        return SelectedTrendingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.selected_trend_holder, parent, false
            ))
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: SelectedTrendingViewHolder, position: Int) {
        val user_id = postsList.get(position).user_id
        val timeAgo = TimeAgo.using(postsList.get(position).timestamp)
        val caption = postsList.get(position).caption
        val content = postsList.get(position).content

        val hashtag = caption.substringAfter("#").substringBefore(" ")

        holder.selectedTrendingHolderBinding.textView33.text = timeAgo

        if (caption.contains("#", ignoreCase = true)){
            val end = hashtag.length + 1
            val start = caption.indexOf("#")


            val spannableString = SpannableString(caption)
            val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#03A9F4"))
            if (start > -1){
                spannableString.setSpan(foregroundColorSpan, start, start+end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.selectedTrendingHolderBinding.textView34.text = spannableString
            }

        }

        if (content.equals("no_image")){
            holder.selectedTrendingHolderBinding.imageView17.visibility = View.GONE
        }
        else{
            holder.selectedTrendingHolderBinding.imageView17.visibility = View.VISIBLE
            Glide.with(ctx!!).load(content).into(holder.selectedTrendingHolderBinding.imageView17)
        }

        fdb?.child("Details")?.child(user_id)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val name = p0.child("username").value.toString()
                    val fname = p0.child("firstname").value.toString()
                    val lname = p0.child("lastname").value.toString()
                    val prof_pic = p0.child("profile_picture").value.toString()

                    Glide.with(ctx!!).load(prof_pic).into(holder.selectedTrendingHolderBinding.profilePicId)
                    holder.selectedTrendingHolderBinding.textView29.text = "$fname $lname"
                    holder.selectedTrendingHolderBinding.textView32.text = "@$name"
                }
            }

        })



    }

}