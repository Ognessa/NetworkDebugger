package com.ognessa.network_debugger.ui.http

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ognessa.network_debugger.R
import com.ognessa.network_debugger.model.DetailsTabTypes
import com.ognessa.network_debugger.model.HttpRequestEntity
import com.ognessa.network_debugger.theme.LocalDimens

@Composable
internal fun HttpDetailsScreen(
    entity: HttpRequestEntity,
    onBackPress: () -> Unit,
    onCopy: (String) -> Unit
) {
    val tabs = DetailsTabTypes.entries
    var tabIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Toolbar(
                entity = entity,
                onBackPress = onBackPress,
                onCopy = onCopy
            )

            TabsBlock(
                tabs = tabs,
                tabIndex = tabIndex,
                onClick = { tabIndex = it }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(LocalDimens.current.transactionItemsSpaceSize)
            ) {
                if (tabs[tabIndex] == DetailsTabTypes.OVERVIEW) {
                    OverviewBlock(entity = entity)
                }

                if (tabs[tabIndex] == DetailsTabTypes.REQUEST) {
                    RequestBlock(
                        modifier = Modifier.fillMaxWidth(),
                        entity = entity
                    )
                }

                if (tabs[tabIndex] == DetailsTabTypes.RESPONSE) {
                    ResponseBlock(
                        modifier = Modifier.fillMaxWidth(),
                        entity = entity
                    )
                }
            }
        }
    }
}

@Composable
private fun Toolbar(
    entity: HttpRequestEntity,
    onBackPress: () -> Unit,
    onCopy: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = LocalDimens.current.toolbarHorizontalPadding,
                vertical = LocalDimens.current.toolbarVerticalPadding
            ),
        verticalArrangement = Arrangement.spacedBy(LocalDimens.current.toolbarSpace)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(LocalDimens.current.iconSize)
                    .clickable { onBackPress() }
                    .padding(LocalDimens.current.iconPadding),
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null
            )

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.http_details_title),
                style = MaterialTheme.typography.titleLarge
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Icon(
                    modifier = Modifier
                        .size(LocalDimens.current.iconSize)
                        .clickable { onCopy(entity.allDataAsString()) }
                        .padding(LocalDimens.current.iconPadding),
                    painter = painterResource(R.drawable.ic_copy),
                    contentDescription = null
                )
            }
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = entity.getRequestUrl(),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
private fun TabsBlock(
    tabs: List<DetailsTabTypes>,
    tabIndex: Int,
    onClick: (index: Int) -> Unit
) {
    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = tabIndex
    ) {
        tabs.forEachIndexed { index, it ->
            Tab(
                modifier = Modifier.padding(LocalDimens.current.tabsPadding),
                selected = tabs[tabIndex] == it,
                onClick = { onClick(index) }
            ) {
                Text(
                    text = when (it) {
                        DetailsTabTypes.OVERVIEW -> stringResource(R.string.overview_title)
                        DetailsTabTypes.REQUEST -> stringResource(R.string.request_title)
                        DetailsTabTypes.RESPONSE -> stringResource(R.string.response_title)
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

private fun LazyListScope.OverviewBlock(
    entity: HttpRequestEntity
) {
    item {
        Item(
            title = "Method",
            value = entity.getRequestMethod()
        )
    }

    item {
        Item(
            title = "Protocol",
            value = entity.getProtocol()
        )
    }

    item {
        Item(
            title = "Status",
            value = entity.getStatus().toString()
        )
    }

    item {
        Item(
            title = "Response code",
            value = entity.getResponseCode().toString()
        )
    }
    item {
        Item(
            title = "SSL",
            value = if (entity.isSsl()) "Yes" else "No"
        )
    }

    item {
        Item(
            title = "Request time",
            value = entity.getRequestDate()
        )
    }

    item {
        Item(
            title = "Response time",
            value = entity.getResponseDate()
        )
    }

    item {
        Item(
            title = "Duration",
            value = "${entity.getTookMs()} ms"
        )
    }

    item {
        Item(
            title = "Request size",
            value = entity.getRequestSizeString()
        )
    }

    item {
        Item(
            title = "Response size",
            value = entity.getResponseSizeString()
        )
    }

    item {
        Item(
            title = "Total size",
            value = entity.getTotalSizeString()
        )
    }
}

private fun LazyListScope.RequestBlock(
    modifier: Modifier = Modifier,
    entity: HttpRequestEntity
) {
    val headers = entity.getRequestHeadersString()
    val body = entity.getFormattedRequestBody()
    val isPlain = entity.getRequestBodyIsPlainText()

    if (headers.isNotEmpty()) {
        item {
            Text(
                modifier = modifier.padding(horizontal = LocalDimens.current.contentHorizontalPadding),
                text = headers
            )
        }
    }

    item {
        Text(
            modifier = modifier.padding(horizontal = LocalDimens.current.contentHorizontalPadding),
            text = if (isPlain.not()) "(encoded or binary body omitted)" else body
        )
    }
}

private fun LazyListScope.ResponseBlock(
    modifier: Modifier = Modifier,
    entity: HttpRequestEntity
) {
    val headers = entity.getResponseHeadersString()
    val body = entity.getResponseBody()
    val isPlain = entity.getResponseBodyIsPlainText()

    //TODO optimize
//    if (headers.isNotEmpty()) {
//        item {
//            Text(
//                modifier = modifier.padding(horizontal = LocalDimens.current.contentHorizontalPadding),
//                text = headers
//            )
//        }
//    }

    item {
        Text(
            modifier = modifier.padding(horizontal = LocalDimens.current.contentHorizontalPadding),
            text = if (isPlain.not()) "(encoded or binary body omitted)" else body
        )
    }
}

@Composable
private fun Item(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LocalDimens.current.contentHorizontalPadding)
    ) {
        Text(
            modifier = Modifier.width(150.dp),
            text = "[$title]"
        )
        Text(
            modifier = Modifier.weight(1f),
            text = value
        )
    }
}