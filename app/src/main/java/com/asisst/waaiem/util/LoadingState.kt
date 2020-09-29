package com.asisst.waaiem.util

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}
class LoadingState(val status : Status, val msg : String) {

    companion object {
        val LOADING : LoadingState
        val LOADED : LoadingState
        val ERROR : LoadingState

    init {
        LOADING = LoadingState(Status.RUNNING, "Loading")
        LOADED = LoadingState(Status.RUNNING, "Success")
        ERROR = LoadingState(Status.RUNNING, "Something went wrong")
    }
    }
}