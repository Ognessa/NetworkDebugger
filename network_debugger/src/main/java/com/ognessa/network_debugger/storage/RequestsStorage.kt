package com.ognessa.network_debugger.storage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ognessa.network_debugger.external.NetworkMonitorConfig
import com.ognessa.network_debugger.model.HttpRequestEntity
import com.ognessa.network_debugger.model.SocketRequestEntity
import com.ognessa.network_debugger.ui.activity.NetworkDebuggerActivity
import com.ognessa.network_debugger.R

internal object RequestsStorage {

    private var channel: NotificationChannel? = null

    private val httpRequests = mutableListOf<HttpRequestEntity>()
    private val socketRequests = mutableListOf<SocketRequestEntity>()

    fun addHttpRequest(
        context: Context,
        entity: HttpRequestEntity
    ) {
        httpRequests.add(0, entity)
        if (NetworkMonitorConfig.getNotificationVisibility() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            showNotification(context)
        }
    }

    fun addSocketRequest(
        context: Context,
        entity: SocketRequestEntity
    ) {
        socketRequests.add(0, entity)
        if (NetworkMonitorConfig.getNotificationVisibility() &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            showNotification(context)
        }
    }

    fun getHttpRequestList(): List<HttpRequestEntity> = httpRequests.toList()

    fun getSocketRequestsList(): List<SocketRequestEntity> = socketRequests.toList()

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showNotification(
        context: Context
    ) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val list = mNotificationManager.activeNotifications.map { it.id }

        if (list.contains(NETWORK_DEBUGGER_ID).not()) {
            if (channel == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = NotificationChannel(
                    NETWORK_DEBUGGER_CHANNEL,
                    "Some name",
                    NotificationManager.IMPORTANCE_HIGH
                )

                channel?.let {
                    it.enableLights(true)
                    it.lightColor = Color.RED
                    it.enableVibration(false)
                    mNotificationManager.createNotificationChannel(it)
                }
            }

            val intent = Intent(context, NetworkDebuggerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pi = PendingIntent.getActivity(
                context, NETWORK_DEBUGGER_ID, intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val mBuilder = NotificationCompat.Builder(context, NETWORK_DEBUGGER_CHANNEL)
                .setSmallIcon(R.drawable.ic_bug)
                .setContentTitle("Network monitor")
                .setContentText("Tap to open monitor")
                .setContentIntent(pi)
                .setAutoCancel(false)

            mNotificationManager.notify(NETWORK_DEBUGGER_ID, mBuilder.build())
        }
    }

    private const val NETWORK_DEBUGGER_CHANNEL = "network_debugger_channel"
    private const val NETWORK_DEBUGGER_ID = 123
}