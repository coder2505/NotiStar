package com.example.notistar.Services

import android.icu.util.GregorianCalendar
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notistar.data.database.RoomEntity
import com.example.notistar.data.repository.UpdateDBWithIncomingNotifications
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var updateDBWithIncomingNotifications: UpdateDBWithIncomingNotifications

    companion object {
        private const val TAG = "NOTIFICATION LISTENER"
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn != null) {

            serviceScope.launch {

                Log.d(
                    TAG,
                    "onNotificationPosted: ${sbn.notification.extras.getString("android.title")}"
                )
                Log.d(
                    TAG,
                    "onNotificationPosted: ${sbn.notification.extras.getString("android.text")}"
                )
                Log.d(TAG, "onNotificationPosted: ${sbn.packageName}")
                val timestampMillis = sbn.postTime

                val calendar = GregorianCalendar()
                calendar.timeInMillis = timestampMillis

                val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val readableTime = formatter.format(calendar.time)

                Log.d(TAG, "onNotificationPosted: Time: $readableTime")

                val notification = RoomEntity(
                    packageName = sbn.packageName,
                    title = sbn.notification.extras.getString("android.title") ?: "Not Given",
                    textDesc = sbn.notification.extras.getString("android.text"),
                    time = readableTime
                )

                updateDBWithIncomingNotifications.insertNotificationInDB(notification)


            }


        }
    }
}