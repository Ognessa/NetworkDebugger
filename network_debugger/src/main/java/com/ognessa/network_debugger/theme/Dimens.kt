package com.ognessa.network_debugger.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalDimens = compositionLocalOf { Dimens() }

internal class Dimens {
    val toolbarHorizontalPadding: Dp = 16.dp
    val toolbarVerticalPadding: Dp = 12.dp

    val tabsPadding: Dp = 8.dp

    val transactionIndicatorWidth: Dp = 20.dp
    val transactionItemsSpaceSize: Dp = 8.dp
}