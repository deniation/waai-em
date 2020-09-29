package com.asisst.waaiem.user.main.pages.home.page

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.MainActivity
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ActivityCommentsBinding
import com.asisst.waaiem.databinding.ActivityLoginBinding
import com.asisst.waaiem.user.authentication.pages.LoginActivity
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.authentication.viewmodelfactory.ProfileViewmodelFactory
import com.asisst.waaiem.user.main.pages.profile.viewmodel.ProfileViewmodel
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_comments.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CommentsActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()

    private val factory by instance<ProfileViewmodelFactory>()

    private lateinit var viewModel: ProfileViewmodel

    private var content: String? = null
    private var caption: String? = null
    private var timeAgo: String? = null
    private var username: String? = null
    private var fullname: String? = null
    private var profPic: String? = null
    private var flames: String? = null
    private var spice: String? = null
    private var stale: String? = null
    private var postId: String? = null
    private var userId: String? = null

    private lateinit var firebaseDatabase : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCommentsBinding = DataBindingUtil.setContentView(this, R.layout.activity_comments)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewmodel::class.java)
        binding.viewmodel = viewModel

        content = intent.getStringExtra("content")
        caption = intent.getStringExtra("caption")
        timeAgo = intent.getStringExtra("time_ago")
        username = intent.getStringExtra("uid")
        fullname = intent.getStringExtra("fname")
        profPic = intent.getStringExtra("pic")
        flames = intent.getStringExtra("flames")
        spice = intent.getStringExtra("spice")
        stale = intent.getStringExtra("stale")
        postId = intent.getStringExtra("id")
        userId = intent.getStringExtra("user_id")

        viewModel.postId = postId
        viewModel.comments_content_view = comments_view
        viewModel.ctx = this

        viewModel.getComments()

        imageView8.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        imageView9.setOnClickListener {
            firebaseDatabase = FirebaseDatabase.getInstance().reference
            val push_id = firebaseDatabase.child("Users").child("Comments").child(postId!!).push().key.toString()
            val comment = editTextTextMultiLine2.getText().toString()
            if (comment.isNotEmpty()){
                val commentMap = mapOf("comment" to comment, "comment_id" to push_id, "user_id" to userId!!,
                    "post_id" to postId, "timestamp" to ServerValue.TIMESTAMP)


                firebaseDatabase.child("Users").child("Comments").child(postId!!).child(push_id).setValue(commentMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        editTextTextMultiLine2.text = null
                    }else{
                        Toast.makeText(this, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    }

    override fun onStart() {
        super.onStart()

        val hashtag = caption?.substringAfter("#")?.substringBefore(" ")
        val atname = caption?.substringAfter("@")?.substringBefore(" ")
        val link = caption?.substringAfter("www.")?.substringBefore(" ")

        Glide.with(this).load(profPic).into(posts_prof_pic_id)
        posts_username_id.text = fullname
        posts_name_id.text = "@"+username
        posts_time_id.text = timeAgo

        if (content.equals("no_image")){
            posts_image_view_id.visibility = View.GONE
        }
        else{
            posts_image_view_id.visibility = View.VISIBLE
            Glide.with(this).load(content).into(posts_image_view_id)
        }

        if (caption.equals("no_caption")) {
            posts_caption_id.visibility = View.GONE
        }
        else{
            if (caption?.contains("#", ignoreCase = true)!!){
                val end = hashtag?.length?.plus(1)
                val start = caption?.indexOf("#")


                val spannableString = SpannableString(caption)
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#0070C0"))
                if (start!! > -1){
                    spannableString.setSpan(foregroundColorSpan, start, start+ end!!, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    posts_caption_id.text = spannableString
                }

            }
            else if (caption?.contains("@", ignoreCase = true)!!){
                val end = atname?.length!! + 1
                val start = caption?.indexOf("@")


                val spannableString = SpannableString(caption)
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#0070C0"))
                if (start!! > -1){
                    spannableString.setSpan(foregroundColorSpan, start, start+end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    posts_caption_id.text = spannableString
                }
            }
            else if (caption?.contains("www.", ignoreCase = true)!!){
                val end = link?.length!! + 4
                val start = caption?.indexOf("www.")


                val spannableString = SpannableString(caption)
                val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#0070C0"))
                if (start!! > -1){
                    spannableString.setSpan(foregroundColorSpan, start, start+end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    posts_caption_id.text = spannableString
                }
            }
            else {
                posts_caption_id.text = caption
            }

        }

        if (flames?.toInt()!! > 1){
            posts_flame_count.text = "$flames Flames"
        }else{
            posts_flame_count.text = "$flames Flame"
        }

        if (flames.equals("0")){
            posts_flame_id.setImageResource(R.drawable.ic_flame)
        }else{
            posts_flame_id.setImageResource(R.drawable.ic_flames_selected)
        }

        if (spice?.toInt()!! > 1){
            posts_spice_count.text = "$spice Recooks"
        }else{
            posts_spice_count.text = "$spice Recook"
        }

        if (spice.equals("0")){
            posts_spice_id.setImageResource(R.drawable.ic_spice_unselected)
        }else{
            posts_spice_id.setImageResource(R.drawable.ic_spice_selected)
        }

        if (stale?.toInt()!! > 1){
            posts_stale_count.text = "$stale Stales"
        }else{
            posts_stale_count.text = "$stale Stale"
        }

        if (stale.equals("0")){
            posts_stale_id.setImageResource(R.drawable.ic_stale_unselected)
        }else{
            posts_stale_id.setImageResource(R.drawable.ic_stale_selected)
        }

    }
}