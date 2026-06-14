package com.weibox.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weibox.app.data.model.WeiboPost
import com.weibox.app.data.repository.WeiboRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 头像首字母颜色池
private val avatarColors = listOf(
    Color(0xFFFA7D40), Color(0xFF4CAF50), Color(0xFF2196F3),
    Color(0xFF9C27B0), Color(0xFFFF5722), Color(0xFF009688)
)

@Composable
fun PostCard(
    post: WeiboPost,
    onUserClick: (String) -> Unit,
    repo: WeiboRepository? = null,
    modifier: Modifier = Modifier
) {
    var viewerUrls by remember { mutableStateOf<List<String>?>(null) }
    var viewerStart by remember { mutableIntStateOf(0) }
    var showComments by remember { mutableStateOf(false) }

    if (viewerUrls != null) {
        ImageViewerDialog(
            urls = viewerUrls!!,
            initialIndex = viewerStart,
            onDismiss = { viewerUrls = null }
        )
    }
    if (showComments && repo != null) {
        CommentsBottomSheet(
            postId = post.id,
            commentCount = post.commentsCount,
            repo = repo,
            onDismiss = { showComments = false }
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                // 头像：优先网络图，否则显示首字母圆圈
                UserAvatar(
                    avatarUrl = post.userAvatar,
                    name = post.userName,
                    size = 42,
                    onClick = { if (post.userId.isNotEmpty()) onUserClick(post.userId) }
                )

                Spacer(Modifier.width(10.dp))

                Column(Modifier.weight(1f)) {
                    // 用户名 + 时间
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = post.userName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = formatTime(post.createdAtTimestamp),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    // 正文
                    if (post.text.isNotBlank()) {
                        Text(
                            text = post.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp,
                            maxLines = 8,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(6.dp))
                    }

                    // 图片
                    val pics = post.pics
                    if (pics.isNotEmpty()) {
                        ImageGrid(pics = pics, onImageClick = { idx ->
                            viewerUrls = pics; viewerStart = idx
                        })
                        Spacer(Modifier.height(6.dp))
                    }

                    // 转发卡片
                    post.retweetPost?.let { rt ->
                        RetweetCard(
                            post = rt,
                            onUserClick = onUserClick,
                            onImageClick = { idx ->
                                viewerUrls = rt.pics; viewerStart = idx
                            }
                        )
                        Spacer(Modifier.height(6.dp))
                    }

                    // 操作栏
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ActionItem(
                            icon = Icons.Outlined.Repeat,
                            label = formatCount(post.repostsCount)
                        )
                        ActionItem(
                            icon = Icons.Outlined.ChatBubbleOutline,
                            label = formatCount(post.commentsCount),
                            onClick = if (repo != null && post.commentsCount > 0) {
                                { showComments = true }
                            } else null
                        )
                        ActionItem(
                            icon = Icons.Outlined.FavoriteBorder,
                            label = formatCount(post.likesCount)
                        )
                    }
                }
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 0.5.dp
            )
        }
    }
}

@Composable
fun UserAvatar(
    avatarUrl: String,
    name: String,
    size: Int = 40,
    onClick: (() -> Unit)? = null
) {
    val colorIndex = (name.firstOrNull()?.code ?: 0) % avatarColors.size
    val bgColor = avatarColors[colorIndex]
    val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        if (avatarUrl.isNotBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = initial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size * 0.4f).sp
            )
        }
    }
}

@Composable
private fun RetweetCard(
    post: WeiboPost,
    onUserClick: (String) -> Unit,
    onImageClick: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(Modifier.padding(10.dp)) {
            if (post.userName.isNotBlank()) {
                Text(
                    text = "@${post.userName}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { if (post.userId.isNotEmpty()) onUserClick(post.userId) }
                )
                Spacer(Modifier.height(3.dp))
            }
            if (post.text.isNotBlank()) {
                Text(
                    text = post.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (post.pics.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                ImageGrid(pics = post.pics, onImageClick = onImageClick, maxSize = 80)
            }
        }
    }
}

@Composable
private fun ImageGrid(
    pics: List<String>,
    onImageClick: (Int) -> Unit,
    maxSize: Int = 110
) {
    val count = pics.size.coerceAtMost(9)
    val cols = when (count) { 1 -> 1; 4 -> 2; else -> 3 }
    val itemSize = when (cols) { 1 -> 200.dp; 2 -> 130.dp; else -> maxSize.dp }

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        for (row in 0 until ((count + cols - 1) / cols)) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                for (col in 0 until cols) {
                    val idx = row * cols + col
                    if (idx < count) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(pics[idx]).crossfade(true).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(itemSize)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.outline)
                                .clickable { onImageClick(idx) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 2.dp, horizontal = 4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (onClick != null) MaterialTheme.colorScheme.primary
                   else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        if (label != "0") {
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatCount(n: Int): String = when {
    n <= 0    -> "0"
    n >= 10000 -> "${n / 10000}万"
    else      -> n.toString()
}

private fun formatTime(ts: Long): String {
    if (ts == 0L) return ""
    val now = System.currentTimeMillis()
    val diff = now - ts
    return when {
        diff < 60_000      -> "刚刚"
        diff < 3600_000    -> "${diff / 60_000}分钟前"
        diff < 86400_000   -> "${diff / 3600_000}小时前"
        diff < 86400_000 * 7 -> "${diff / 86400_000}天前"
        else -> SimpleDateFormat("MM-dd", Locale.CHINA).format(Date(ts))
    }
}
