package com.tejuzumaki.directdrop

import android.content.Intent
import android.webkit.JavascriptInterface
import androidx.core.content.ContextCompat

class DirectDropBridge(private val context: android.content.Context) {
    @JavascriptInterface
    fun showNotification(peerName: String, message: String) {
        val intent = Intent(context, DirectDropForegroundService::class.java)
        intent.action = "SHOW_NOTIFICATION"
        intent.putExtra("peer_name", peerName)
        intent.putExtra("message", message)
        ContextCompat.startForegroundService(context, intent)
    }

    @JavascriptInterface
    fun startRinging(peerName: String) {
        val intent = Intent(context, DirectDropForegroundService::class.java)
        intent.action = "START_RINGING"
        intent.putExtra("peer_name", peerName)
        ContextCompat.startForegroundService(context, intent)
    }

    @JavascriptInterface
    fun stopRinging() {
        val intent = Intent(context, DirectDropForegroundService::class.java)
        intent.action = "STOP_RINGING"
        ContextCompat.startForegroundService(context, intent)
    }
}
