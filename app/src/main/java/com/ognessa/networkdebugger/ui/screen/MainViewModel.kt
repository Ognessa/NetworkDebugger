package com.ognessa.networkdebugger.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {

    fun sendPostRequest() {
        Log.d("DEBUG", "POST request")
    }
}