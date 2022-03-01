package com.kanyideveloper.twitterspacesclone.screens.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginVm @Inject constructor() : ViewModel() {
    fun login(name: String) {
        val safeName = if (name.isEmpty()) "Android User" else name
        //_navigate.value = Screen.Loading(safeName)
    }
}