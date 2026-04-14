package com.example.notistar
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class NotificationListener : NotificationListenerService() {

    companion object {
       private const val TAG = "NOTIFICATION LISTENER"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d(TAG, "onNotificationPosted: ${sbn.toString()}")
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

        Log.d(TAG, sbn.toString())
    }
}