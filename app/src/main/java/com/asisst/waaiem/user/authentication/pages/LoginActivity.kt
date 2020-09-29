package com.asisst.waaiem.user.authentication.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.MainActivity
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ActivityLoginBinding
import com.asisst.waaiem.user.authentication.authinterface.AuthListener
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.authentication.viewmodelfactory.AuthViewmodelFactory
import com.asisst.waaiem.user.authentication.viewmodelfactory.HomeViewmodelFactory
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()

    private val factory by instance<AuthViewmodelFactory>()

    private lateinit var viewModel: AuthViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewmodel::class.java)
        binding.viewmodel = viewModel
        viewModel.activity = this
        viewModel.loading_btn = login_btn_id
    }

}