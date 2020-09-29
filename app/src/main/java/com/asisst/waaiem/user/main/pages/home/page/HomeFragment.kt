package com.asisst.waaiem.user.main.pages.home.page

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentHomeBinding
import com.asisst.waaiem.user.authentication.authinterface.AuthListener
import com.asisst.waaiem.user.authentication.viewmodelfactory.HomeViewmodelFactory
import com.asisst.waaiem.user.main.pages.home.viewmodel.HomeViewmodel
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<HomeViewmodelFactory>()

    private lateinit var viewModel: HomeViewmodel

    var uri : Uri? = null
    companion object{
        private val SELECT_PROFILE_PICTURE = 1
        private val STORAGE_PERMISSIONS = 2
        private val SELECT_GIF_PICTURE = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.profile_pic = user_profile_pic_id
        viewModel.post_panel = current_user_details
        viewModel.ctx = this.context
        viewModel.activity = this.activity
        viewModel.social_content_view = users_content_view_id
        viewModel.search = editText17
        viewModel.post_loading_btn = button9
        viewModel.all_options = m_options
        viewModel.others_options = posts_more_layout
        viewModel.self_options = posts_m_layout
        viewModel.close_others_options = imageView14o
        viewModel.close_self_options = imageView14
        viewModel.follow = imageView42
        viewModel.unfollow = imageView48
        viewModel.follow_count = textView140
        viewModel.unfollow_count = textView141
        viewModel.delete = imageView37
        viewModel.getPosts()

        user_post_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    SELECT_PROFILE_PICTURE
                )
            }else{
                requestStoragePermission()
            }
        }

        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                val intent = Intent()
                intent.type = "gif/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    SELECT_GIF_PICTURE
                )
            }else{
                requestStoragePermission()
            }
        }

    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)){

        }else{
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSIONS
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SELECT_PROFILE_PICTURE && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data
            if (uri != null){
                Glide.with(this).load(uri).into(user_image_preview_id)
                user_image_preview_id.visibility = View.VISIBLE
                viewModel.profile_picture = uri
            }else{
                Toast.makeText(activity, "fail", Toast.LENGTH_LONG).show()
            }
        }

        if(requestCode == SELECT_GIF_PICTURE && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data
            if (uri != null){
                Glide.with(this).asGif().load(uri).into(user_image_preview_id)
                user_image_preview_id.visibility = View.VISIBLE
                viewModel.profile_picture = uri
            }else{
                Toast.makeText(activity, "fail", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSIONS){
            if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}