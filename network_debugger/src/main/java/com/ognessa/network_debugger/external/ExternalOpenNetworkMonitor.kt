package com.ognessa.network_debugger.external

import android.content.Context
import android.content.Intent
import com.ognessa.network_debugger.ui.activity.NetworkDebuggerActivity

fun Context.openNetworkMonitor() {
    startActivity(
        Intent(
            this,
            NetworkDebuggerActivity::class.java
        )
    )
}