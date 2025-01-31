package com.ognessa.network_debugger.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

internal val Purple80 = Color(0xFFD0BCFF)
internal val PurpleGrey80 = Color(0xFFCCC2DC)
internal val Pink80 = Color(0xFFEFB8C8)

internal val Purple40 = Color(0xFF6650a4)
internal val PurpleGrey40 = Color(0xFF625b71)
internal val Pink40 = Color(0xFF7D5260)

internal val LocalColors = compositionLocalOf { Colors() }

internal class Colors {
    val transactionStatusDefault = Color(0xFF212121)
    val transactionStatusRequested = Color(0xFF9E9E9E)
    val transactionStatusError = Color(0xFFF44336)
    val transactionStatus500 = Color(0xFFB71C1C)
    val transactionStatus400 = Color(0xFFFF9800)
    val transactionStatus300 = Color(0xFF0D47A1)
    val transactionStatus200 = Color(0xFF43B410)
}