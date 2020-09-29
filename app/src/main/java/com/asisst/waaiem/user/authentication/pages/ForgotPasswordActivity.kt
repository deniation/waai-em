package com.asisst.waaiem.user.authentication.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ActivityForgotPasswordBinding
import com.asisst.waaiem.databinding.ActivityLoginBinding
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.authentication.viewmodelfactory.AuthViewmodelFactory
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ForgotPasswordActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()

    private val factory by instance<AuthViewmodelFactory>()

    private lateinit var viewModel: AuthViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityForgotPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewmodel::class.java)
        binding.viewmodel = viewModel
        viewModel.activity = this
        viewModel.loading_btn = button2
    }
}