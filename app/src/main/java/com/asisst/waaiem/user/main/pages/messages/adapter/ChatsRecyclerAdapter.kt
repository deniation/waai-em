package com.asisst.waaiem.user.main.pages.messages.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.MessageViewBinding
import com.asisst.waaiem.user.main.pages.messages.pages.ConversationActivity
import com.asisst.waaiem.user.main.pages.messages.pojo.Chats
import com.asisst.waaiem.user.main.pages.messages.pojo.Messages
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatsRecyclerAdapter(
    private val chats: List<Chats>,
    private val activity: Activity?
) : RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder>() {
    private var ctx : Context? = null
    var user_auth : FirebaseAuth? = null
    var user_id :String? = null
    var firebaseDatabase : DatabaseReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        ctx = parent.context
        user_auth = FirebaseAuth.getInstance()

        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        firebaseDatabase?.keepSynced(true)

        if (user_auth!!.currentUser != null){
            user_id = user_auth!!.currentUser?.uid
        }
        return ChatsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.message_view, parent, false
            ))    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        val recepient = chats.get(position).to

        val query = firebaseDatabase?.child("Messages")?.child(user_id!!)?.child(recepient!!)?.orderByChild("timestamp")?.limitToFirst(1)
        query?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (data in p0.children) {
                        val msg = data.getValue(Messages::class.java)
                        val from = msg?.from
                        val message = msg?.message
                        val timestamp = msg?.timestamp
                        val timeAgo = TimeAgo.using(timestamp!!)

                        holder.messageViewBinding.textView125.text = message
                        holder.messageViewBinding.textView127.text = timeAgo
                    }
                }else{
                }
            }
        })

        firebaseDatabase?.child("Details")?.child(recepient!!)?.addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val pic = p0.child("profile_picture").value.toString()
                    Glide.with(ctx!!).load(pic).into(holder.messageViewBinding.msgSenderProfPicId)
                }
            }

        })

        holder.messageViewBinding.root.setOnClickListener {
            val id = holder.adapterPosition
            val userId = chats.get(id).to

            firebaseDatabase?.child("Details")?.child(userId!!)?.addValueEventListener(object :
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
    inner class ChatsViewHolder(val messageViewBinding: MessageViewBinding) : RecyclerView.ViewHolder(messageViewBinding.root)

}