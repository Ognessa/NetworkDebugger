package com.ognessa.networkdebugger.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ognessa.networkdebugger.network.http.repository.JsonPlaceholderRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: JsonPlaceholderRepository
) : ViewModel() {

    fun sendGetRequest() {
        viewModelScope.launch {
            repository.getPostsList()
        }
    }

    fun sendPostRequest() {
        viewModelScope.launch {
            repository.sendPost()
        }
    }

    fun sendDeleteRequest() {
        viewModelScope.launch {
            repository.deletePost()
        }
    }
}