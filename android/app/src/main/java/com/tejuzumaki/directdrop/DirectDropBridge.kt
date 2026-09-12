package com.tejuzumaki.directdrop

import android.content.Intent
import android.os.Build
import android.webkit.JavascriptInterface

class DirectDropBridge(private val context: android.content.Context) {
    @JavascriptInterface
    fun showNotification(peerName: String, message: String) {
        val intent = Intent(context, DirectDropForegroundService::class.java)
        intent.action = "SHOW_NOTIFICATION"
        intent.putExtra("peer_name", peerName)
        intent.putExtra("message", message)
        startServiceSafely(intent)
    }

    @JavascriptInterface
    fun startRinging(peerName: String) {
        val intent = Intent(context, DirectDropForegroundService::class.java)
        intent.action = "START_RINGING"
        intent.putExtra("peer_name", peerName)
        startServiceSafely(intent)
    }

    @JavascriptInterface
    fun stopRinging() {
        val intent = Intent(context, DirectDropForegroundService::class.java)
        intent.action = "STOP_RINGING"
        startServiceSafely(intent)
    }

    private fun startServiceSafely(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
