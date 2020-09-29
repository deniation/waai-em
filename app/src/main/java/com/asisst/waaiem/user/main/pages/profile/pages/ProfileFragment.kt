package com.asisst.waaiem.user.main.pages.profile.pages

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentProfileBinding
import com.asisst.waaiem.user.authentication.viewmodelfactory.ProfileViewmodelFactory
import com.asisst.waaiem.user.main.pages.profile.adapter.ProfilePagerAdapter
import com.asisst.waaiem.user.main.pages.profile.viewmodel.ProfileViewmodel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ProfileFragment : Fragment(), KodeinAware {
    override val kodein by kodein()

    private val factory by instance<ProfileViewmodelFactory>()

    private lateinit var viewModel: ProfileViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.ctx = this.context
        viewModel.profile_pic = profile_pic_id
        viewModel.username_profile = textView6
        viewModel.fullname_profile =  profile_username_id
        viewModel.username_location = profile_location_id
        viewModel.edit_profile_pic = edit_pic_id
        viewModel.edit_username_profile = editTextTextPersonName
        viewModel.edit_firstname_profile =  editTextTextPersonName2
        viewModel.edit_lastname_profile = editTextTextPersonName3
        viewModel.edit_phone_profile =  editTextPhone
        viewModel.edit_bio_profile = editTextTextMultiLine
        viewModel.t_follower = textView130
        viewModel.t_followings = textView129
        viewModel.edit_profile_layout = edit_profile_layout
        viewModel.loading_btn = button2
        viewModel.getUserProfile()

        (activity as AppCompatActivity).setSupportActionBar(profile_toolbar)

        user_profile_pager.adapter = ProfilePagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(user_profile_tabs, user_profile_pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when (position) {
                0 -> tab.text = "In My Kitchen"
                1-> tab.text = "Invited Kitchens"
                2-> tab.text = "Settings"
            }
        }).attach()

        button.setOnClickListener {
            viewModel.openEditProfilePanel()
        }

        imageView7.setOnClickListener {
            viewModel.closeEditProfilePanel()
        }

        button2.setOnClickListener {
            button2.startAnimation()
            if (viewModel.firstname.isNullOrEmpty() && viewModel.lastname.isNullOrEmpty() && viewModel.username.isNullOrEmpty() &&
                viewModel.phone_number.isNullOrEmpty() && viewModel.bio.isNullOrEmpty()) {
                Toast.makeText(view.context, "No changes made", Toast.LENGTH_LONG).show()
                button2.revertAnimation()
                return@setOnClickListener
            }
            val thread = object : Thread() {
                override fun run() {
                    super.run()
                    try {
                        Thread.sleep(6000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            button2.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.logo))
            viewModel.closeEditProfilePanel()
            Toast.makeText(view.context, "Profile Updated Successful", Toast.LENGTH_LONG).show()
            thread.start()
        }
    }
}