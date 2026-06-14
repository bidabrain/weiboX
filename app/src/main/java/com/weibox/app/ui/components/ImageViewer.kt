package com.weibox.app.ui.components

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageViewerDialog(
    urls: List<String>,
    initialIndex: Int = 0,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = initialIndex) { urls.size }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableImage(
                    url = urls[page],
                    onLongClick = {
                        scope.launch { saveImageToGallery(context, urls[page]) }
                    }
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "关闭", tint = Color.White)
            }

            if (urls.size > 1) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${urls.size}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ZoomableImage(url: String, onLongClick: () -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // 离开页面时重置缩放
    DisposableEffect(Unit) {
        onDispose { scale = 1f; offset = Offset.Zero }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(onClick = {}, onLongClick = onLongClick)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)

                    do {
                        val event = awaitPointerEvent()
                        val active = event.changes.filter { it.pressed }

                        when {
                            active.size >= 2 -> {
                                // 双指捏合：缩放 + 平移
                                val newZoom = event.calculateZoom()
                                val prevCentroid = event.calculateCentroid(useCurrent = false)
                                val centroid = event.calculateCentroid(useCurrent = true)

                                val newScale = (scale * newZoom).coerceIn(1f, 5f)
                                scale = newScale
                                offset = if (newScale > 1f) offset + (centroid - prevCentroid)
                                         else Offset.Zero

                                // 消费事件（阻止 Pager 响应捏合）
                                event.changes.forEach { it.consume() }
                            }

                            active.size == 1 && scale > 1f -> {
                                // 单指平移（放大状态下）
                                val change = active.first()
                                offset += change.positionChange()
                                change.consume()
                            }

                            // 单指 + 未放大：不消费 → 事件传给 HorizontalPager → 正常翻页
                        }
                    } while (event.changes.any { it.pressed })
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url).crossfade(true).build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
        )
    }
}

private suspend fun saveImageToGallery(context: Context, url: String) {
    withContext(Dispatchers.IO) {
        try {
            val loader = coil.ImageLoader(context)
            val req = ImageRequest.Builder(context).data(url).allowHardware(false).build()
            val bitmap = ((loader.execute(req) as? SuccessResult)?.drawable
                    as? android.graphics.drawable.BitmapDrawable)?.bitmap
                ?: run {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
                    }
                    return@withContext
                }

            val filename = "WeiboX_${System.currentTimeMillis()}.jpg"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/WeiboX")
                }
                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                uri?.let { context.contentResolver.openOutputStream(it) }?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it)
                }
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.insertImage(
                    context.contentResolver, bitmap, filename, "WeiboX image"
                )
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "已保存到相册", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "保存失败: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
