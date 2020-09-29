package com.asisst.metimesocial.ym.user.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.TrendingHolderBinding
import com.asisst.waaiem.user.main.pages.home.pojo.Posts
import com.asisst.waaiem.user.main.pages.trending.adapter.SelectedTrendRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TrendRecyclerAdapter(
    private val posts: List<String>,
    private val moreOptions: ConstraintLayout,
    private val selectedTrend: CoordinatorLayout,
    private val selected_trend_content_view : RecyclerView
) : RecyclerView.Adapter<TrendRecyclerAdapter.TrendingViewHolder>(){
    private var ctx : Context? = null
    var user_auth : FirebaseAuth? = null
    var user_id :String? = null
    var fdb : DatabaseReference? = null
    var  posts_list : MutableList<Posts> = mutableListOf()
    var mLinearLayoutManager : LinearLayoutManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        ctx = parent.context
        user_auth = FirebaseAuth.getInstance()
        fdb = FirebaseDatabase.getInstance().reference

        if (user_auth!!.currentUser != null){
            user_id = user_auth!!.currentUser?.uid
        }
        return TrendingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.trending_holder, parent, false
            ))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        val h_tag = posts.get(position)
        if (holder.adapterPosition == 3){
            holder.trendingHolderBinding.textView25.text = "42K Flames"
        } else if(holder.adapterPosition == 2){
            holder.trendingHolderBinding.textView25.text = "12K Flames"
        }else if(holder.adapterPosition == 1){
            holder.trendingHolderBinding.textView25.text = "2K Flames"
        }else if(holder.adapterPosition == 0){
            holder.trendingHolderBinding.textView25.text = "420K Flames"
        }else{
            holder.trendingHolderBinding.textView25.text = "1K Flames"
        }

        holder.trendingHolderBinding.textView24.text = h_tag

        holder.trendingHolderBinding.imageView13.setOnClickListener {
            if (moreOptions.visibility == View.GONE){
                val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in)
                moreOptions.animation = animation
                moreOptions.visibility = View.VISIBLE
            }else{
                val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
                moreOptions.animation = animation
                moreOptions.visibility = View.GONE
            }
        }

        holder.trendingHolderBinding.root.setOnClickListener {
            val trend_pos = holder.adapterPosition
            val selected_trend = posts.get(trend_pos)
            if (selectedTrend.visibility == View.GONE){
                val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in)
                selectedTrend.animation = animation
                selectedTrend.visibility = View.VISIBLE
                fdb?.child("Users")?.child("Posts")?.addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()){
                            posts_list.clear()
                            for (data in p0.children) {
                                val hashtags = data.getValue(Posts::class.java)
                                val caption = hashtags?.caption
                                    if (caption!!.contains("#", ignoreCase = true)){
                                        val hashtag = caption.substringAfter("#").substringBefore(" ")
                                        if (hashtag.equals(selected_trend)){
                                            posts_list.add(hashtags)
                                        }else{
                                            return
                                        }
                                    }else{
                                        return
                                    }
                            }

                            selected_trend_content_view.also {
                                mLinearLayoutManager = LinearLayoutManager(ctx!!)
                                mLinearLayoutManager!!.reverseLayout = true
                                mLinearLayoutManager!!.stackFromEnd = true
                                it.layoutManager = mLinearLayoutManager
                                it.setHasFixedSize(true)
                                it.adapter = SelectedTrendRecyclerAdapter(posts_list)
                            }
                        }
                    }
                })

            }else{
                val animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out)
                selectedTrend.animation = animation
                selectedTrend.visibility = View.GONE
            }

        }



    }

    inner class TrendingViewHolder(val trendingHolderBinding: TrendingHolderBinding) : RecyclerView.ViewHolder(trendingHolderBinding.root)
}