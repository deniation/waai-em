package com.asisst.waaiem.user.authentication.pages

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ActivityLoginBinding
import com.asisst.waaiem.databinding.ActivityPersonalInformationBinding
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.authentication.viewmodelfactory.AuthViewmodelFactory
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_personal_information.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class PersonalInformationActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<AuthViewmodelFactory>()

    private lateinit var viewModel: AuthViewmodel

    private var uri : Uri? = null

    companion object{
        private val SELECT_PROFILE_PICTURE = 1
        private val STORAGE_PERMISSIONS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityPersonalInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_information)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewmodel::class.java)
        binding.viewmodel = viewModel
        viewModel.activity = this
        viewModel.loading_btn = personal_signup_btn_id

        attach_prof_pic.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PROFILE_PICTURE)
            }else{
                requestStoragePermission()
            }
        }

    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSIONS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SELECT_PROFILE_PICTURE && resultCode == Activity.RESULT_OK && data != null){
            uri = data.data
            if (uri != null){
                Glide.with(this).load(uri).placeholder(R.drawable.attach).into(attach_prof_pic)
                //LoadProfilePicture(attach_prof_pic, uri!!)
                viewModel.profile_picture = uri
            }else{
                Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSIONS){
            if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}