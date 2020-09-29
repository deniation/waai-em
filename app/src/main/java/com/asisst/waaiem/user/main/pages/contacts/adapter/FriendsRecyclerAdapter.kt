package com.asisst.waaiem.user.main.pages.contacts.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FriendsListBinding
import com.asisst.waaiem.user.main.pages.contacts.pojo.Followers
import com.asisst.waaiem.user.main.pages.messages.pages.ConversationActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class FriendsRecyclerAdapter(private val followers: List<Followers>, private val activity: Activity?) : RecyclerView.Adapter<FriendsRecyclerAdapter.FriendsListViewHolder>(){
    private var ctx : Context? = null

    var fdb : DatabaseReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsListViewHolder {
        ctx = parent.context

        fdb = FirebaseDatabase.getInstance().reference.child("Users")


        return FriendsListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.friends_list, parent, false))

    }

    override fun getItemCount(): Int {
        return followers.size
    }

    override fun onBindViewHolder(holder: FriendsListViewHolder, position: Int) {
        val user_id = followers.get(position).post_owner_id

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


                    Glide.with(ctx!!).load(prof_pic).into(holder.friendsListBinding.profilePicId)
                    holder.friendsListBinding.textView38.text = "$fname $lname"
                    holder.friendsListBinding.textView39.text = "@$name"
                }
            }

        })

        holder.friendsListBinding.root.setOnClickListener {
            val id = holder.adapterPosition
            val userId = followers.get(id).post_owner_id

            fdb?.child("Details")?.child(userId!!)?.addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()){
                        val pic = p0.child("profile_picture").value.toString()
                        val fname = p0.child("firstname").value.toString()
                        val lname = p0.child("lastname").value.toString()
                        val username = p0.child("username").value.toString()
                        val fullname = "$fname $lname"

                        val intent = Intent(activity, ConversationActivity::class.java)
                        intent.putExtra("image", pic)
                        intent.putExtra("id", userId)
                        intent.putExtra("fullname", fullname)
                        intent.putExtra("username", username)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                }

            })
        }
    }

    inner class FriendsListViewHolder(val friendsListBinding: FriendsListBinding) : RecyclerView.ViewHolder(friendsListBinding.root)

}