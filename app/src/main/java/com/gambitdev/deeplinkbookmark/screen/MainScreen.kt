package com.gambitdev.deeplinkbookmark.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.gambitdev.deeplinkbookmark.ui.theme.DeeplinkBookmarkTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(addShortcut: (Bitmap, String, String) -> Unit) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {

        val labelState = remember { mutableStateOf("") }
        val linkState = remember { mutableStateOf("") }
        val imageUrlState = remember { mutableStateOf("") }
        val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
        val selectedItem = remember { mutableStateOf(Icons.Default.AccessAlarm) }

        val coroutineScope = rememberCoroutineScope()
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = SheetState(
                skipPartiallyExpanded = false, density = LocalDensity.current
            )
        )

        BottomSheetScaffold(scaffoldState = bottomSheetScaffoldState, sheetContent = {
            IconSelector { selectedIcon ->
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.hide()
                    // Handle selected icon...
                    println("Selected Icon: $selectedIcon")
                    selectedItem.value = selectedIcon
                }
            }
        }) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(value = labelState.value,
                    onValueChange = { labelState.value = it },
                    label = { Text(text = "Label") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = linkState.value,
                    onValueChange = { linkState.value = it },
                    label = { Text(text = "Deeplink") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(value = imageUrlState.value,
                    onValueChange = { imageUrlState.value = it },
                    label = { Text(text = "Image Url") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val imageLoader = ImageLoader(context)
                    val request =
                        ImageRequest.Builder(context).data(imageUrl).size(PixelSize(256, 256))
                            .scale(Scale.FILL).build()

                    val result = imageLoader.execute(request).drawable
                    bitmap = withContext(Dispatchers.IO) { result?.toBitmap() }

                }) {
                    Text("Load url")
                }

                if (bitmap.value != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
//                Button(onClick = {
//                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
//                }) {
//                    Text("Show Icon Selector")
//                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val label = labelState.value
                        val link = linkState.value

                        addShortcut(bitmap, label, link)
                    }, modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Submit")
                }

            }
        }
    }


}


@Composable
fun IconSelector(onIconSelected: (ImageVector) -> Unit) {
    val icons = IconList.filled
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
    ) {
        items(icons.size) { index ->
            val icon = icons[index]
            IconButton(onClick = { onIconSelected(icon) }) {
                Icon(icon, contentDescription = "Icon Button")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DeeplinkBookmarkTheme {
        MainScreen { _, _, _ -> }
    }
}
