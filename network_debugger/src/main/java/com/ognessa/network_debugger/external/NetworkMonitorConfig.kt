package com.ognessa.network_debugger.external

object NetworkMonitorConfig {
    private var showNotification: Boolean = true

    fun setNotificationVisibility(show: Boolean) {
        showNotification = show
    }

    internal fun getNotificationVisibility(): Boolean {
        return showNotification
    }
}