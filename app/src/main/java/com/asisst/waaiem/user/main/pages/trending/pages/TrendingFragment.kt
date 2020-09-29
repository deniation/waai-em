package com.asisst.waaiem.user.main.pages.trending.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.asisst.waaiem.R
import com.asisst.waaiem.databinding.FragmentTrendingBinding
import com.asisst.waaiem.user.authentication.viewmodelfactory.TrendingViewmodelFactory
import com.asisst.waaiem.user.main.pages.trending.viewmodel.TrendingViewmodel
import kotlinx.android.synthetic.main.fragment_trending.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class TrendingFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory by instance<TrendingViewmodelFactory>()

    private lateinit var viewModel: TrendingViewmodel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTrendingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_trending, container, false)
        val view: View = binding.root
        viewModel = ViewModelProviders.of(this, factory).get(TrendingViewmodel::class.java)
        binding.viewmodel = viewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.ctx = this.context
        viewModel.activity = this.activity
        viewModel.trend_content_view = trending_view
        viewModel.more_options = more_options
        viewModel.selected_trend_content_view = selected_trend
        viewModel.selectedTrend = trending_layout
        viewModel.user_preview_content_view = discover_people_id
        viewModel.all_user_content_view = discover_more
        viewModel.getHashtags()
        viewModel.getContacts()

        imageView14.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this.context, R.anim.fade_out)
            more_options.animation = animation
            more_options.visibility = View.GONE
        }
    }

}