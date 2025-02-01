package com.ognessa.networkdebugger.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ognessa.network_debugger.external.openNetworkMonitor
import com.ognessa.networkdebugger.ui.theme.NetworkDebuggerExampleTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetworkDebuggerExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenContent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun ScreenContent(
        modifier: Modifier = Modifier
    ) {
        var showHttpDialog by remember { mutableStateOf(false) }

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TODO continue here add http requests
            Button(
                modifier = Modifier,
                onClick = { showHttpDialog = true }
            ) {
                Text(text = "Send http request")
            }

            Button(
                modifier = Modifier,
                onClick = { openNetworkMonitor() }
            ) {
                Text(text = "Open network monitor")
            }
        }

        if (showHttpDialog) {
            HttpRequestsDialog(
                onDismiss = { showHttpDialog = false }
            )
        }
    }

    @Composable
    private fun HttpRequestsDialog(
        onDismiss: () -> Unit
    ) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16)
                    )
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.sendGetRequest()
                        onDismiss()
                    }
                ) {
                    Text(text = "GET request")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.sendPostRequest()
                        onDismiss()
                    }
                ) {
                    Text(text = "POST request")
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.sendDeleteRequest()
                        onDismiss()
                    }
                ) {
                    Text(text = "DELETE request")
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        NetworkDebuggerExampleTheme {
            ScreenContent(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

