package com.example.notistar.Services
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class NotificationListener : NotificationListenerService() {

    companion object {
       private const val TAG = "NOTIFICATION LISTENER"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn != null) {
            Log.d(TAG, "onNotificationPosted: ${sbn.packageName}, ${sbn.notification.extras.getString("android.text")}")
        }
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

        if (sbn != null) {
            Log.d(TAG, "REMOVED NOTIFS ${sbn.notification.extras}")
        }
    }

    override fun getActiveNotifications(): Array<StatusBarNotification> {

        Log.d(TAG, "getActiveNotifications: ${super.getActiveNotifications()}")
        return super.getActiveNotifications()
    }
}