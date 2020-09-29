package com.asisst.waaiem.user.authentication.repository

import android.net.Uri
import com.asisst.waaiem.user.authentication.firebase.AuthFirebase

class AuthRepository(private val firebase: AuthFirebase) {
    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun login(email: String, password: String) = firebase.login(email, password)

    fun forgotPassword(email: String) = firebase.forgotPassword(email)

    fun register(email: String, password: String, username: String, firstname: String, lastname: String) = firebase.register(email, password, username, firstname, lastname)

    fun profileSetup(profile_picture: Uri, phone_number: String, recovery_email: String, location: String) = firebase.profileSetup(profile_picture, phone_number, recovery_email, location)

    fun morePersonalInformation(gender: String, birthday_date: String, occupation: String, residence: String, relationship: String, education: String, religion: String, languages_spoken: String,
                                favourite_sport_team: String, favourite_color: String, favourite_book: String, favourite_movie: String, favourite_tv_show: String, favourite_music_genre: String, favourite_food: String,
                                favourite_drink: String, favourite_travel_destination: String) = firebase.morePersonalInformation(gender, birthday_date, occupation, residence, relationship, education, religion, languages_spoken,
        favourite_sport_team, favourite_color, favourite_book, favourite_movie, favourite_tv_show, favourite_music_genre, favourite_food, favourite_drink, favourite_travel_destination)

    fun getLoadingState() = firebase.loadingState
}