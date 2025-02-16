package com.ognessa.network_debugger.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ognessa.network_debugger.R
import com.ognessa.network_debugger.model.HttpRequestEntity
import com.ognessa.network_debugger.model.SocketRequestEntity
import com.ognessa.network_debugger.model.TabTypes
import com.ognessa.network_debugger.storage.RequestsStorage
import com.ognessa.network_debugger.theme.LocalColors
import com.ognessa.network_debugger.theme.LocalDimens

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen({}, {})
}

@Composable
internal fun MainScreen(
    onHttpRequestPressed: (HttpRequestEntity) -> Unit,
    onSocketRequestPressed: (SocketRequestEntity) -> Unit
) {
    val tabs by remember { mutableStateOf(TabTypes.entries) }
    var selected by remember { mutableStateOf(TabTypes.HTTP) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MainToolbar(modifier = Modifier.fillMaxWidth())
            MainTabs(
                modifier = Modifier.fillMaxWidth(),
                tabs = tabs,
                selected = selected,
                onClick = { selected = it }
            )
            RequestsList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                httpList = RequestsStorage.getHttpRequestList(),
                socketList = RequestsStorage.getSocketRequestsList(),
                selectedTab = selected,
                onHttpRequestPressed = onHttpRequestPressed,
                onSocketRequestPressed = onSocketRequestPressed
            )
        }
    }
}

@Composable
private fun MainToolbar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(
            horizontal = LocalDimens.current.toolbarHorizontalPadding,
            vertical = LocalDimens.current.toolbarVerticalPadding
        )
    ) {
        Text(
            text = stringResource(R.string.main_screen_title),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun MainTabs(
    modifier: Modifier = Modifier,
    tabs: List<TabTypes> = emptyList(),
    selected: TabTypes = TabTypes.HTTP,
    onClick: (TabTypes) -> Unit = {}
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = tabs.indexOfFirst { it == selected }.let { index ->
            if (index != -1) index else 0
        }
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = tab == selected,
                onClick = { onClick(tab) }
            ) {
                Text(
                    modifier = Modifier.padding(LocalDimens.current.tabsPadding),
                    text = when (tab) {
                        TabTypes.HTTP -> stringResource(R.string.http_list_title)
                        TabTypes.SOCKET -> stringResource(R.string.socket_list_title)
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun RequestsList(
    modifier: Modifier = Modifier,
    httpList: List<HttpRequestEntity>,
    socketList: List<SocketRequestEntity>,
    selectedTab: TabTypes,
    onHttpRequestPressed: (HttpRequestEntity) -> Unit,
    onSocketRequestPressed: (SocketRequestEntity) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        when (selectedTab) {
            TabTypes.HTTP -> {
                httpList.forEach { item ->
                    HttpRequestItem(
                        modifier = Modifier.fillMaxWidth(),
                        entity = item,
                        onClick = { onHttpRequestPressed(item) }
                    )
                }
            }

            TabTypes.SOCKET -> {
                socketList.forEach { item ->
                    SocketRequestItem(
                        modifier = Modifier.fillMaxWidth(),
                        entity = item,
                        onClick = { onSocketRequestPressed(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HttpRequestItem(
    modifier: Modifier = Modifier,
    entity: HttpRequestEntity,
    onClick: () -> Unit
) {
    val statusColor = when {
        entity.getStatus() == HttpRequestEntity.Status.Failed -> LocalColors.current.transactionStatusError
        entity.getStatus() == HttpRequestEntity.Status.Requested -> LocalColors.current.transactionStatusRequested
        entity.getResponseCode() >= 500 -> LocalColors.current.transactionStatus500
        entity.getResponseCode() >= 400 -> LocalColors.current.transactionStatus400
        entity.getResponseCode() >= 300 -> LocalColors.current.transactionStatus300
        entity.getResponseCode() >= 200 -> LocalColors.current.transactionStatus200
        else -> LocalColors.current.transactionStatusDefault
    }

    Row(modifier = modifier
        .height(IntrinsicSize.Min)
        .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(LocalDimens.current.transactionIndicatorWidth)
                .background(statusColor)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(LocalDimens.current.transactionItemsSpaceSize)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = entity.getRequestMethod(),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "${entity.getRequestDate()} (${entity.getTookMs()}ms)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(LocalDimens.current.transactionItemsSpaceSize))
            Text(
                text = entity.getRequestUrl(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun SocketRequestItem(
    modifier: Modifier = Modifier,
    entity: SocketRequestEntity,
    onClick: () -> Unit
) {
    //TODO
}

