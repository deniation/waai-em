package com.asisst.waaiem.user.authentication.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.asisst.waaiem.MainActivity
import com.asisst.waaiem.R
import com.asisst.waaiem.user.authentication.authinterface.AuthListener
import com.asisst.waaiem.user.authentication.pages.LoginActivity
import com.asisst.waaiem.user.authentication.pages.MorePersonalInformationActivity
import com.asisst.waaiem.user.authentication.pages.PersonalInformationActivity
import com.asisst.waaiem.user.authentication.repository.AuthRepository
import com.asisst.waaiem.util.LoadingState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class AuthViewmodel(private val authRepository: AuthRepository) : ViewModel() {
    var email: String? = null
    var password: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var username: String? = null
    var confirm_password: String? = null
    var phone_number : String? = null
    var location : String? = null
    var profile_picture : Uri? = null
    var gender : String? = null
    var birthday_date : String? = null
    var occupation : String? = null
    var residence : String? = null
    var relationship : String? = null
    var education : String? = null
    var religion : String? = null
    var languages_spoken : String? = null
    var favourite_sport_team : String? = null
    var favourite_color : String? = null
    var favourite_book : String? = null
    var favourite_movie : String? = null
    var favourite_tv_show : String? = null
    var favourite_music_genre : String? = null
    var favourite_food : String? = null
    var favourite_drink : String? = null
    var favourite_travel_destination : String? = null
    var favourite_player : String? = null
    var favourite_comics_studio : String? = null
    var favourite_bread : String? = null

    var activity : Activity? = null

    var loading_btn : CircularProgressButton? = null
    var skip_loading_btn : CircularProgressButton? = null

    var spinnerReligion : Spinner? = null
    var spinnerRelationship : Spinner? = null
    var spinnerEducation : Spinner? = null
    var spinnerGender : Spinner? = null

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    var authListener: AuthListener? = null

    val user by lazy {
        authRepository.currentUser()
    }

    val loadingState : LiveData<LoadingState> by lazy{
        authRepository.getLoadingState()
    }

    //function to perform login
    fun login(view : View) {
         loading_btn?.startAnimation()
        //validating email and password
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(view.context, "Email or Password Required", Toast.LENGTH_LONG).show()
            loading_btn?.revertAnimation()
            return
        }

        //calling login from repository to perform the actual authentication
        val disposable = authRepository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                if (it.equals("User")) {
                    Toast.makeText(view.context, it, Toast.LENGTH_LONG).show()
                    val intent = Intent(view.context, MainActivity::class.java)
                    view.context.startActivity(intent)
                    activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_check))
                }else if (it.equals("Vendor")) {
                }else{
                }
            }, {
                //sending a failure callback
                loading_btn?.revertAnimation()
                Toast.makeText(view.context, "Email or Password Required", Toast.LENGTH_LONG).show()
            })
        disposables.add(disposable)
    }

    fun forgotPassword(view : View) {
        loading_btn?.startAnimation()
        //validating email and password
        if (email.isNullOrEmpty()) {
            Toast.makeText(view.context, "Email Required", Toast.LENGTH_LONG).show()
            loading_btn?.revertAnimation()
            return
        }

        //authentication started
        //calling login from repository to perform the actual authentication
        val disposable = authRepository.forgotPassword(email!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback

                val intent = Intent(view.context, LoginActivity::class.java)
                view.context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                Toast.makeText(view.context, "Email link sent to your email account", Toast.LENGTH_LONG).show()
                loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_check))
            }, {
                //sending a failure callback
                Toast.makeText(view.context, it.message!!, Toast.LENGTH_LONG).show()
                loading_btn?.revertAnimation()
            })
        disposables.add(disposable)
    }

    fun signup(view : View) {
        loading_btn?.startAnimation()
        if (firstname.isNullOrEmpty() || lastname.isNullOrEmpty() || email.isNullOrEmpty() || username.isNullOrEmpty() ||
            password.isNullOrEmpty() || confirm_password.isNullOrEmpty()) {
            Toast.makeText(view.context, "Please input all values", Toast.LENGTH_LONG).show()
            loading_btn?.revertAnimation()
            return
        }
        if (!password.equals(confirm_password)){
            Toast.makeText(view.context, "Passwords do not match", Toast.LENGTH_LONG).show()
            loading_btn?.revertAnimation()
            return
        }
        val disposable = authRepository.register(email!!, password!!, username!!, firstname!!, lastname!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val intent = Intent(view.context, PersonalInformationActivity::class.java)
                view.context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                Toast.makeText(view.context, "Saved Successful", Toast.LENGTH_LONG).show()
                loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_check))

            }, {
                Toast.makeText(view.context, it.message!!, Toast.LENGTH_LONG).show()
                loading_btn?.revertAnimation()
            })
        disposables.add(disposable)
    }

    fun profileSetup(view : View) {
        loading_btn?.startAnimation()
        if (profile_picture == null || email.isNullOrEmpty() || phone_number.isNullOrEmpty() || location.isNullOrEmpty()) {
            Toast.makeText(view.context, "Please input all values", Toast.LENGTH_LONG).show()
            loading_btn?.revertAnimation()
            return
        }
        val disposable = authRepository.profileSetup(profile_picture!!, email!!, phone_number!!, location!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val intent = Intent(view.context, MorePersonalInformationActivity::class.java)
                view.context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                Toast.makeText(view.context, "Saved Successful", Toast.LENGTH_LONG).show()
                loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_check))
            }, {
                Toast.makeText(view.context, it.message!!, Toast.LENGTH_LONG).show()
                loading_btn?.revertAnimation()
            })
        disposables.add(disposable)
    }

    fun morePersonalInformation(view : View) {
        loading_btn?.startAnimation()
        if (gender.isNullOrEmpty()) {
            gender = "N/A"
        }
        if (birthday_date.isNullOrEmpty()){
            birthday_date = "N/A"
        }
        if (occupation.isNullOrEmpty()){
            occupation = "N/A"
        }
        if (residence.isNullOrEmpty()){
            residence = "N/A"
        }
        if (relationship.isNullOrEmpty()){
            relationship = "N/A"
        }
        if (education.isNullOrEmpty()){
            education = "N/A"
        }
        if (religion.isNullOrEmpty()){
            religion = "N/A"
        }
        if (languages_spoken.isNullOrEmpty()){
            languages_spoken = "N/A"
        }
        if (favourite_book.isNullOrEmpty()){
            favourite_book = "N/A"
        }
        if (favourite_sport_team.isNullOrEmpty()){
            favourite_sport_team = "N/A"
        }
        if (favourite_color.isNullOrEmpty()){
            favourite_color = "N/A"
        }
        if (favourite_movie.isNullOrEmpty()){
            favourite_movie = "N/A"
        }
        if (favourite_music_genre.isNullOrEmpty()){
            favourite_music_genre = "N/A"
        }
        if (favourite_tv_show.isNullOrEmpty()){
            favourite_tv_show = "N/A"
        }
        if (favourite_drink.isNullOrEmpty()){
            favourite_drink = "N/A"
        }
        if (favourite_food.isNullOrEmpty()){
            favourite_food = "N/A"
        }
        if (favourite_travel_destination.isNullOrEmpty()){
            favourite_travel_destination = "N/A"
        }
        authListener?.onStarted()
        val disposable = authRepository.morePersonalInformation(gender!!, birthday_date!!, occupation!!, residence!!, relationship!!, education!!, religion!!, languages_spoken!!,
            favourite_sport_team!!, favourite_color!!, favourite_book!!, favourite_movie!!, favourite_tv_show!!, favourite_music_genre!!, favourite_food!!, favourite_drink!!, favourite_travel_destination!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val intent = Intent(view.context, MainActivity::class.java)
                view.context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                Toast.makeText(view.context, "Account Created Successful", Toast.LENGTH_LONG).show()
                loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_check))
            }, {
                Toast.makeText(view.context, it.message!!, Toast.LENGTH_LONG).show()
                loading_btn?.revertAnimation()
            })
        disposables.add(disposable)
    }

    fun skipMorePersonalInformation(view : View) {
        skip_loading_btn?.startAnimation()
        if (gender.isNullOrEmpty()) {
            gender = "N/A"
        }
        if (birthday_date.isNullOrEmpty()){
            birthday_date = "N/A"
        }
        if (occupation.isNullOrEmpty()){
            occupation = "N/A"
        }
        if (residence.isNullOrEmpty()){
            residence = "N/A"
        }
        if (relationship.isNullOrEmpty()){
            relationship = "N/A"
        }
        if (education.isNullOrEmpty()){
            education = "N/A"
        }
        if (religion.isNullOrEmpty()){
            religion = "N/A"
        }
        if (languages_spoken.isNullOrEmpty()){
            languages_spoken = "N/A"
        }
        if (favourite_book.isNullOrEmpty()){
            favourite_book = "N/A"
        }
        if (favourite_sport_team.isNullOrEmpty()){
            favourite_sport_team = "N/A"
        }
        if (favourite_color.isNullOrEmpty()){
            favourite_color = "N/A"
        }
        if (favourite_movie.isNullOrEmpty()){
            favourite_movie = "N/A"
        }
        if (favourite_music_genre.isNullOrEmpty()){
            favourite_music_genre = "N/A"
        }
        if (favourite_tv_show.isNullOrEmpty()){
            favourite_tv_show = "N/A"
        }
        if (favourite_drink.isNullOrEmpty()){
            favourite_drink = "N/A"
        }
        if (favourite_food.isNullOrEmpty()){
            favourite_food = "N/A"
        }
        if (favourite_travel_destination.isNullOrEmpty()){
            favourite_travel_destination = "N/A"
        }
        authListener?.onStarted()
        val disposable = authRepository.morePersonalInformation(gender!!, birthday_date!!, occupation!!, residence!!, relationship!!, education!!, religion!!, languages_spoken!!,
            favourite_sport_team!!, favourite_color!!, favourite_book!!, favourite_movie!!, favourite_tv_show!!, favourite_music_genre!!, favourite_food!!, favourite_drink!!, favourite_travel_destination!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val intent = Intent(view.context, MainActivity::class.java)
                view.context.startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                Toast.makeText(view.context, "Account Created Successful", Toast.LENGTH_LONG).show()
                skip_loading_btn?.doneLoadingAnimation(Color.parseColor("#C00000"), BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_check))
            }, {
                Toast.makeText(view.context, it.message!!, Toast.LENGTH_LONG).show()
                skip_loading_btn?.revertAnimation()
            })
        disposables.add(disposable)
    }

    //SPINNERS

    fun spinnerGenderClicked(context: Context){
        spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item_category = context.resources.getStringArray(R.array.gender)
                gender = item_category[position]
            }

        }
    }

    fun spinnerRelationshipClicked(context: Context){
        spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item_category = context.resources.getStringArray(R.array.relationship)
                relationship = item_category[position]
            }

        }
    }

    fun spinnerReligionClicked(context: Context){
        spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item_category = context.resources.getStringArray(R.array.religion)
                religion = item_category[position]
            }

        }
    }

    fun spinnerEducationClicked(context: Context){
        spinnerGender?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item_category = context.resources.getStringArray(R.array.education)
                education = item_category[position]
            }

        }
    }
}