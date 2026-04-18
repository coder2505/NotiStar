package com.example.notistar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notistar.ViewModels.PermissionCheck
import com.example.notistar.ui.theme.NotiStarTheme
import dagger.hilt.android.HiltAndroidApp



class MainActivity : ComponentActivity() {

    private val viewmodel: PermissionCheck by viewModels()

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

                            Text("FAHHHHHH ")

                        }
                    }
                }

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
