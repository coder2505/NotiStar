package com.example.notistar.Services
import android.icu.util.GregorianCalendar
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale


class NotificationListener : NotificationListenerService() {

    companion object {
       private const val TAG = "NOTIFICATION LISTENER"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn != null) {
            Log.d(TAG, "onNotificationPosted: ${sbn.notification.extras.getString("android.title")}")
            Log.d(TAG, "onNotificationPosted: ${sbn.notification.extras.getString("android.text")}")
            Log.d(TAG, "onNotificationPosted: ${sbn.packageName}")
            val timestampMillis = sbn.postTime

            val calendar = GregorianCalendar()
            calendar.timeInMillis = timestampMillis

            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
            val readableTime = formatter.format(calendar.time)

            Log.d(TAG, "onNotificationPosted: Time: $readableTime")

        }
    }
}