package com.gambitdev.deeplinkbookmark

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.gambitdev.deeplinkbookmark.ui.theme.DeeplinkBookmarkTheme

enum class CustomIcons(val label: String, @DrawableRes val res: Int) {
    Clipboard(label = "Clipboard", res = R.drawable.clipboard),
    Home(label = "Home", res = R.drawable.home),
    ID(label = "ID", res = R.drawable.id)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this

        setContent {
            DeeplinkBookmarkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val labelState = remember { mutableStateOf("") }
                    val linkState = remember { mutableStateOf("") }
                    val items = CustomIcons.values()
                    val selectedItem = remember { mutableStateOf(items[0]) }

                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = labelState.value,
                            onValueChange = { labelState.value = it },
                            label = { Text(text = "Label") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = linkState.value,
                            onValueChange = { linkState.value = it },
                            label = { Text(text = "Deeplink") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        DropdownMenuBox(items = items, selectedItem = selectedItem)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val label = labelState.value
                                val link = linkState.value
                                addShortcut(
                                    drawable = ContextCompat.getDrawable(
                                        context,
                                        selectedItem.value.res
                                    )!!,
                                    label = label,
                                    link = link
                                )
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "Submit")
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun DropdownMenuBox(items: Array<CustomIcons>, selectedItem: MutableState<CustomIcons>) {
        var expanded by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground)
                .wrapContentSize(Alignment.TopStart)
        ) {
            Text(
                text = selectedItem.value.label,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .padding(16.dp)
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.label) },
                        onClick = {
                            selectedItem.value = item
                            expanded = false
                        })
                }
            }
        }
    }

    private fun addShortcut(drawable: Drawable, label: String, link: String) {
        val shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        // Convert the Bitmap into an Icon
        val icon = Icon.createWithBitmap(bitmap)
        val shortcut = ShortcutInfo.Builder(this, label)
            .setShortLabel(label)
            .setIcon(icon)
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(link)
                )
            )
            .build()

        shortcutManager.dynamicShortcuts = listOf(shortcut)
        val shortcutCallbackIntent = shortcutManager.createShortcutResultIntent(shortcut)
        val permissionIntent = PendingIntent.getBroadcast(
            this,
            0,
            shortcutCallbackIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        shortcutManager.requestPinShortcut(shortcut, permissionIntent.intentSender)
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DeeplinkBookmarkTheme {
        Greeting("Android")
    }
}