package com.example.notistar
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class NotificationListener : NotificationListenerService() {


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

        Log.d("NOTIFICATION CANCELLED", sbn.toString())
    }
}