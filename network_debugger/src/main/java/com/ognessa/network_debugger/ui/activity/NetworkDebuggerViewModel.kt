package com.ognessa.network_debugger.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NetworkDebuggerViewModel : ViewModel() {

    private val _message = MutableLiveData("")
    val message = _message

    fun onCopy(
        context: Context,
        text: String
    ) {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val clipboard = context.getSystemService(ClipboardManager::class.java)
                val clip = ClipData.newPlainText(BUFFER_MESSAGE, text)
                clipboard.setPrimaryClip(clip)

                _message.value = "Copied"
            }
        }
    }

    companion object {
        private const val BUFFER_MESSAGE = "network_debugger"
    }
}