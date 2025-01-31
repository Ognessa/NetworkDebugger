package com.ognessa.network_debugger.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ognessa.network_debugger.theme.NetworkDebuggerTheme
import com.ognessa.network_debugger.ui.main.MainScreen

internal class NetworkDebuggerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetworkDebuggerTheme {
                MainScreen()
            }
        }
    }
}