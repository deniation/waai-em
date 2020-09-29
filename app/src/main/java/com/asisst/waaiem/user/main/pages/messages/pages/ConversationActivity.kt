package com.asisst.waaiem.user.main.pages.messages.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ActivityConversationBinding
import com.asisst.waaiem.user.authentication.viewmodelfactory.MessagesViewmodelFactory
import com.asisst.waaiem.user.authentication.viewmodelfactory.NotificationsViewmodelFactory
import com.asisst.waaiem.user.main.pages.messages.viewmodel.MessagesViewmodel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_conversation.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ConversationActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<MessagesViewmodelFactory>()

    private lateinit var viewModel: MessagesViewmodel

    private var profile_pic : String? = null

    private var user_id : String? = null

    private var fullname : String? = null

    private var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityConversationBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversation)
        viewModel = ViewModelProviders.of(this, factory).get(MessagesViewmodel::class.java)
        binding.viewmodel = viewModel
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        profile_pic = intent.getStringExtra("image")
        user_id = intent?.getStringExtra("id")
        fullname = intent?.getStringExtra("fullname")
        username = intent?.getStringExtra("username")

        Glide.with(this).load(profile_pic).into(circleImageView)
        textView132.text = fullname
        viewModel.username = username

        viewModel.ctx = this
        viewModel.activity = this
        viewModel.user_id = user_id
        viewModel.message_area = editText18
        viewModel.msg_content_view = messages_view

        viewModel.getMessages()

        textView41.text = "View @$username Profile"
        textView42.text = "Block @$username"
        textView43.text = "Report @$username"
        textView44.text = "Delete This Conversation"

        imageView38.setOnClickListener {
            if (m_options.visibility == View.VISIBLE){
                val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
                m_options.animation = animation
                m_options.visibility = View.GONE
            }else{
                val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                m_options.animation = animation
                m_options.visibility = View.VISIBLE
            }
        }

        imageView14.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            m_options.animation = animation
            m_options.visibility = View.GONE
        }
    }
}