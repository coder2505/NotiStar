package com.example.notistar

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notistar.ViewModels.DBContents
import com.example.notistar.ViewModels.PermissionCheck
import com.example.notistar.data.database.RoomEntity
import com.example.notistar.ui.theme.NotiStarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewmodel: PermissionCheck by viewModels()
    private val notificationViewModel: DBContents by viewModels()

    companion object {
        private const val TAG = "MAIN ACTIVITY"
    }

    override fun onResume() {
        super.onResume()
        permissionCheck(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val check by viewmodel.hasPermission.collectAsStateWithLifecycle()
            val listNotification by notificationViewModel.provideNotification()
                .collectAsStateWithLifecycle(initialValue = emptyList())


            NotiStarTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    when (check) {
                        false -> {

                            Text("Cannot read notifications, without permission")
                            Button({ startSettingScreen() }) {
                                Text("Give app permissions")
                            }

                        }

                        true -> {

                            MessagesList(listNotification.reversed())

                        }
                    }
                }

            }
        }
    }


    @Preview
    @Composable
    private fun MessagesList(
        listNotification: List<RoomEntity> = mutableListOf(
            RoomEntity(
                packageName = "com.whatsapp",
                title = "Amma",
                textDesc = "When coming home?",
                time = "18:06"
            ),
            RoomEntity(
                packageName = "com.instagram",
                title = "Yukta",
                textDesc = "Mujhe bhot tension ho raha hai",
                time = "14:08"
            )
        )
    ) {
        LazyColumn(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(listNotification) { notification ->

                MessageUI(
                    packageName = notification.packageName,
                    title = notification.title,
                    textDesc = notification.textDesc ?: "null",
                    time = notification.time
                )


            }
        }
    }


    @Preview
    @Composable
    private fun MessageUI(
        packageName: String = "com.whatsapp",
        title: String = "Amma",
        textDesc: String = "When coming home?",
        time: String = "18:06"
    ) {

        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(packageName)
                    Text(time)
                }
                Text(title)
                Text(textDesc)
            }

        }

    }

    private fun permissionCheck(context: Context) {

        if (isNotificationListenerEnabled(context)) {
            Log.d(TAG, "permissionCheck: REQUEST HAS BEEN GRANTED")
            viewmodel.updatePermission(true)
        } else viewmodel.updatePermission(false)

    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val myPackageName = context.packageName
        val enabledPackages = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledPackages.contains(myPackageName)
    }

    private fun startSettingScreen() {

        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)

    }


}
