package com.gambitdev.deeplinkbookmark.screen

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.Modifier
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.size.PixelSize
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun loadImageFromUrl(modifier: Modifier = Modifier, context: Context, imageUrl: String): Bitmap? {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .size(PixelSize(256, 256))
        .scale(Scale.FILL)
        .build()

    val result = imageLoader.execute(request).drawable
    return withContext(Dispatchers.IO) { result?.toBitmap() }
}