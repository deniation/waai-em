package com.asisst.waaiem.user.main.pages.notifications.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.NotificationsViewBinding
import com.asisst.waaiem.user.main.pages.pojo.Notifications
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationRecyclerAdapter(private val notifications: MutableList<Notifications>, private val it: RecyclerView?) : RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationsViewHolder>(){
    private var ctx : Context? = null
    var user_auth : FirebaseAuth? = null
    var user_id :String? = null
    var firebaseDatabase : DatabaseReference? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationRecyclerAdapter.NotificationsViewHolder {
        ctx = parent.context
        user_auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        firebaseDatabase!!.keepSynced(true)

        if (user_auth!!.currentUser != null){
            user_id = user_auth!!.currentUser?.uid
        }
        return NotificationsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.notifications_view, parent, false
            ))
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(
        holder: NotificationRecyclerAdapter.NotificationsViewHolder,
        position: Int
    ) {
        val timestamp = notifications.get(position).timestamp
        val u_id = notifications.get(position).user_id
        val timeAgo = TimeAgo.using(timestamp)

        holder.notificationsViewBinding.textView135.text = timeAgo

        val total = notifications.distinct().toMutableList().size

        holder.notificationsViewBinding.textView134.text = "Your photo has "+total + " flames"

        firebaseDatabase?.child("Details")?.child(u_id)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val name = p0.child("username").value.toString()
                    val fname = p0.child("firstname").value.toString()
                    val lname = p0.child("lastname").value.toString()
                    val prof_pic = p0.child("profile_picture").value.toString()

                    Glide.with(ctx!!).load(prof_pic).into(holder.notificationsViewBinding.profilePicId)
                }
            }

        })
    }

    inner class NotificationsViewHolder(val notificationsViewBinding: NotificationsViewBinding) : RecyclerView.ViewHolder(notificationsViewBinding.root)

}