package com.asisst.waaiem.user.main.pages.contacts.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentFriendsBinding
import com.asisst.waaiem.databinding.FragmentProfileBinding
import com.asisst.waaiem.user.authentication.viewmodelfactory.ContactViewmodelFactory
import com.asisst.waaiem.user.authentication.viewmodelfactory.ProfileViewmodelFactory
import com.asisst.waaiem.user.main.pages.contacts.viewmodel.ContactViewmodel
import com.asisst.waaiem.user.main.pages.profile.viewmodel.ProfileViewmodel
import kotlinx.android.synthetic.main.fragment_friends.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class FriendsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<ContactViewmodelFactory>()

    private lateinit var viewModel: ContactViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentFriendsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(ContactViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.friends_content_view = friends_list_view
        viewModel.ctx = this.context
        viewModel.activity = this.activity
        viewModel.getFollowers()
    }
}