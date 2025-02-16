package com.ognessa.network_debugger.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ognessa.network_debugger.model.ComposeScreenTitles
import com.ognessa.network_debugger.model.HttpRequestEntity
import com.ognessa.network_debugger.model.SocketRequestEntity
import com.ognessa.network_debugger.theme.NetworkDebuggerTheme
import com.ognessa.network_debugger.ui.http.HttpDetailsScreen
import com.ognessa.network_debugger.ui.main.MainScreen

internal class NetworkDebuggerActivity : ComponentActivity() {

    private val viewModel = NetworkDebuggerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Screen() }
        setObservers()
    }

    private fun setObservers() {
        viewModel.message.observe(this) {
            //TODO don't work
            Log.d("DEBUG", "Copied")
            Toast.makeText(this, it, Toast.LENGTH_SHORT)
        }
    }

    override fun onDestroy() {
        viewModel.message.removeObservers(this)
        super.onDestroy()
    }

    @Composable
    private fun Screen() {
        val navController = rememberNavController()
        var selectedHttpRequest by remember { mutableStateOf<HttpRequestEntity?>(null) }
        var selectedSocketRequest by remember { mutableStateOf<SocketRequestEntity?>(null) }

        NetworkDebuggerTheme {
            NavHost(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                startDestination = ComposeScreenTitles.HOME.name,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                composable(ComposeScreenTitles.HOME.name) {
                    MainScreen(
                        onHttpRequestPressed = { data ->
                            selectedHttpRequest = data
                            navController.navigate(ComposeScreenTitles.HTTP_DETAILS.name)
                        },
                        onSocketRequestPressed = { data ->
//                                    selectedSocketRequest = data
//                                    navController.navigate(ComposeScreenTitles.SOCKET_DETAILS.name)
                        }
                    )
                }

                composable(ComposeScreenTitles.HTTP_DETAILS.name) {
                    HttpDetailsScreen(
                        entity = selectedHttpRequest ?: HttpRequestEntity(),
                        onBackPress = { navController.popBackStack() },
                        onCopy = {
                            viewModel.onCopy(
                                context = this@NetworkDebuggerActivity,
                                text = it
                            )
                        }
                    )
                }
            }
        }
    }
}