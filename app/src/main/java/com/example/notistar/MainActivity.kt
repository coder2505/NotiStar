package com.example.notistar

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
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
            var query by rememberSaveable { mutableStateOf("") }
            val filteredList = if (query.isEmpty()) {
                listNotification.reversed()
            } else {
                val q = query.lowercase()
                listNotification.reversed().filter { entity ->
                    entity.packageName.lowercase().contains(q) ||
                            entity.title.lowercase().contains(q) ||
                            entity.textDesc?.lowercase()?.contains(q) == true ||
                            entity.time.lowercase().contains(q)
                }
            }



                NotiStarTheme {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
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

                                    CustomizableSearchBar(
                                        query = query,
                                        onQueryChange = { changedQuery -> query = changedQuery },
                                        onSearch = {},
                                        searchResults = filteredList,
                                        onResultClick = { query -> },
                                        modifier = Modifier
                                    )
                                    MessagesList(listNotification.reversed())

                                }
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


    @Composable
    private fun MessageUI(
        packageName: String = "com.whatsapp",
        title: String = "Amma",
        textDesc: String = "When coming home?",
        time: String = "18:06"
    ) {

        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceVariant

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f),

                        ) {
                        AsyncImage(
                            modifier = Modifier.size(30.dp, 30.dp),
                            model = getAppIconByPackageName(
                                context = LocalContext.current,
                                packageName
                            ),
                            contentDescription = null
                        )
                        Text(
                            title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        time,
                        style = TextStyle(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    )
                }
                Text(textDesc)
            }

        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomizableSearchBar(
        query: String,
        onQueryChange: (String) -> Unit,
        onSearch: (String) -> Unit,
        searchResults: List<RoomEntity>,
        onResultClick: (String) -> Unit,
        modifier: Modifier = Modifier,
        // Customization options
        placeholder: @Composable () -> Unit = { Text("Search") },
        leadingIcon: @Composable (() -> Unit)? = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon: @Composable (() -> Unit)? = null,
        supportingContent: (@Composable (String) -> Unit)? = null,
        leadingContent: (@Composable () -> Unit)? = null,
    ) {
        // Track expanded state of search bar
        var expanded by rememberSaveable { mutableStateOf(false) }

            SearchBar(

                inputField = {
                    SearchBarDefaults.InputField(
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = {
                            onSearch(query)
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        placeholder = placeholder,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon
                    )
                },
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    dividerColor = Color.Transparent
                ),
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                // Show search results in a lazy column for better performance
                LazyColumn {
                    items(count = searchResults.size) { index ->
                        val resultText = searchResults[index].title
                        MessageUI(
                            packageName = searchResults[index].packageName,
                            title = resultText,
                            textDesc = searchResults[index].textDesc ?: "null",
                            time = searchResults[index].time

                        )
                    }
                }
            }
    }


    fun getAppIconByPackageName(context: Context, packageName: String): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
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
