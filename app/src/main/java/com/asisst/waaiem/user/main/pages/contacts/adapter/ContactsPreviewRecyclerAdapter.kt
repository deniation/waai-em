package com.asisst.waaiem.user.main.pages.contacts.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.DiscoverPeopleBinding
import com.asisst.waaiem.user.main.pages.contacts.pojo.Contacts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContactsPreviewRecyclerAdapter(
    private val contacts: List<Contacts>,
    private val user: String?
) : RecyclerView.Adapter<ContactsPreviewRecyclerAdapter.DiscoverPeopleViewHolder>() {

    private var ctx : Context? = null

    var fdb : DatabaseReference? = null
    var auth : FirebaseAuth? = null

    var uid : String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsPreviewRecyclerAdapter.DiscoverPeopleViewHolder {
        ctx = parent.context

        auth = FirebaseAuth.getInstance()
        fdb = FirebaseDatabase.getInstance().reference.child("Users")

        if (auth!!.currentUser != null){
            uid = auth!!.currentUser?.uid
        }

        return DiscoverPeopleViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.discover_people, parent, false))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ContactsPreviewRecyclerAdapter.DiscoverPeopleViewHolder, position: Int) {

        val profile_pic = contacts.get(position).profile_picture
        val firstname = contacts.get(position).firstname
        val lastname = contacts.get(position).lastname
        val username = contacts.get(position).username
        val user_id = contacts.get(position).user_id

        Glide.with(ctx!!).load(profile_pic).into(holder.discoverPeopleBinding.profilePicId)
        holder.discoverPeopleBinding.textView35.text = "$firstname $lastname"
        holder.discoverPeopleBinding.textView36.text = "@$username"
        holder.discoverPeopleBinding.textView37.text = "In a society that will literally judge you for who you are and you ambitions encompasses, be unapologetic"

        /*fdb?.child("Followings")?.child(uid!!)?.child(user_id)?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    holder.discoverPeopleBinding.button3.text = "Following"
                    holder.discoverPeopleBinding.button3.setTextColor(Color.parseColor("#ffffff"))
                    holder.discoverPeopleBinding.button3.setBackgroundResource(R.drawable.login_signup_button_background)
                }else{
                    holder.discoverPeopleBinding.button3.text = "Follow"
                    holder.discoverPeopleBinding.button3.setTextColor(Color.parseColor("#C00000"))
                    holder.discoverPeopleBinding.button3.setBackgroundResource(R.drawable.follow)
                }
            }
        })*/
    }

    inner class DiscoverPeopleViewHolder(val discoverPeopleBinding: DiscoverPeopleBinding) : RecyclerView.ViewHolder(discoverPeopleBinding.root)



}