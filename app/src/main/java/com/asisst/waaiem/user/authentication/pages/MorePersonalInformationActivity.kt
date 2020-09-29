package com.asisst.waaiem.user.authentication.pages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.ActivityLoginBinding
import com.asisst.waaiem.databinding.ActivityMorePersonalInformationBinding
import com.asisst.waaiem.user.authentication.viewmodel.AuthViewmodel
import com.asisst.waaiem.user.authentication.viewmodelfactory.AuthViewmodelFactory
import kotlinx.android.synthetic.main.activity_more_personal_information.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MorePersonalInformationActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<AuthViewmodelFactory>()

    private lateinit var viewModel: AuthViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_personal_information)

        val binding: ActivityMorePersonalInformationBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewmodel::class.java)
        binding.viewmodel = viewModel
        viewModel.activity = this
        viewModel.loading_btn = button6
        viewModel.skip_loading_btn = textView104

        val gender = resources.getStringArray(R.array.gender)
        val gender_adapter = ArrayAdapter(this, R.layout.spinner_text, gender)
        spinner_gender.adapter = gender_adapter

        val relationship = resources.getStringArray(R.array.relationship)
        val relationship_adapter = ArrayAdapter(this, R.layout.spinner_text, relationship)
        spinner_relationship.adapter = relationship_adapter

        val religion = resources.getStringArray(R.array.religion)
        val religion_adapter = ArrayAdapter(this, R.layout.spinner_text, religion)
        spinner_religion.adapter =religion_adapter

        val education = resources.getStringArray(R.array.education)
        val education_adapter = ArrayAdapter(this, R.layout.spinner_text, education)
        spinner_educational.adapter = education_adapter

        viewModel.spinnerGender = spinner_gender
        viewModel.spinnerRelationship= spinner_relationship
        viewModel.spinnerReligion = spinner_religion
        viewModel.spinnerEducation = spinner_educational

        viewModel.spinnerGenderClicked(this)
        viewModel.spinnerEducationClicked(this)
        viewModel.spinnerRelationshipClicked(this)
        viewModel.spinnerReligionClicked(this)

    }
}