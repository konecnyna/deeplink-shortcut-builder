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
import com.gambitdev.deeplinkbookmark.screen.MainScreen
import com.gambitdev.deeplinkbookmark.ui.theme.DeeplinkBookmarkTheme

enum class CustomIcons(val label: String, @DrawableRes val res: Int) {
    Clipboard(label = "Clipboard", res = R.drawable.clipboard),
    Home(label = "Home", res = R.drawable.home),
    ID(label = "ID", res = R.drawable.id)
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DeeplinkBookmarkTheme { MainScreen(::addShortcut) } }
    }


    private fun addShortcut(bitmap: Bitmap, label: String, link: String) {
        val shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
//        val bitmap = Bitmap.createBitmap(
//            drawable.intrinsicWidth,
//            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
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

