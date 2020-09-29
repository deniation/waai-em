package com.asisst.waaiem.user.authentication.authinterface

interface AuthListener {
    fun onStarted()
    fun onSuccess(message: String)
    fun onFailure(message: String)
}