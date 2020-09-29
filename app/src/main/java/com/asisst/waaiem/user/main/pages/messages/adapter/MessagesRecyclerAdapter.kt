package com.asisst.metimesocial.ym.user.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ConversationPageBinding
import com.asisst.waaiem.user.main.pages.messages.pojo.Messages
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.auth.FirebaseAuth

class MessagesRecyclerAdapter(
    private val messages: List<Messages>
) : RecyclerView.Adapter<MessagesRecyclerAdapter.ConversationViewHolder>(){
    private var ctx : Context? = null
    var user_auth : FirebaseAuth? = null
    var user_id :String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        ctx = parent.context
        user_auth = FirebaseAuth.getInstance()

        if (user_auth!!.currentUser != null){
            user_id = user_auth!!.currentUser?.uid
        }
        return ConversationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.conversation_page, parent, false
            ))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val message = messages.get(position).message
        val from = messages.get(position).from
        val timestamp = messages.get(position).timestamp
        val timeAgo = TimeAgo.using(timestamp)

        if (user_id.equals(from)){
            holder.conversationPageBinding.textView6.visibility = View.GONE
            holder.conversationPageBinding.textView7.visibility = View.GONE
            holder.conversationPageBinding.textView131.text = message
            holder.conversationPageBinding.textView133.text = timeAgo
        }else{
            holder.conversationPageBinding.textView131.visibility = View.GONE
            holder.conversationPageBinding.textView133.visibility = View.GONE
            holder.conversationPageBinding.textView6.text = message
            holder.conversationPageBinding.textView7.text = timeAgo
        }


    }

    inner class ConversationViewHolder(val conversationPageBinding: ConversationPageBinding) : RecyclerView.ViewHolder(conversationPageBinding.root)
}