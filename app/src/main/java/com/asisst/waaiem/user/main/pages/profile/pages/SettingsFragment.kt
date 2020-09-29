package com.asisst.waaiem.user.main.pages.profile.pages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentInMyKitchenBinding
import com.asisst.waaiem.databinding.FragmentSettingsBinding
import com.asisst.waaiem.user.authentication.pages.LoginActivity
import com.asisst.waaiem.user.authentication.viewmodelfactory.ProfileViewmodelFactory
import com.asisst.waaiem.user.main.pages.profile.viewmodel.ProfileViewmodel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SettingsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<ProfileViewmodelFactory>()

    private lateinit var viewModel: ProfileViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.activity = this.activity

        button32.setOnClickListener {
            viewModel.logout.also {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                this.activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

}