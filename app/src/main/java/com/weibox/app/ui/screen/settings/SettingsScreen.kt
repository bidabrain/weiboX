package com.weibox.app.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weibox.app.R
import com.weibox.app.ui.components.WeiboTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: SettingsViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    var showRestoreConfirm by remember { mutableStateOf(false) }
    var passVisible by remember { mutableStateOf(false) }

    // 恢复前确认弹窗
    if (showRestoreConfirm) {
        AlertDialog(
            onDismissRequest = { showRestoreConfirm = false },
            title = { Text("确认恢复") },
            text = { Text("恢复将把备份中的用户合并到当前关注列表（不会删除已有关注）。确认继续？") },
            confirmButton = {
                TextButton(onClick = { showRestoreConfirm = false; vm.restore() }) {
                    Text("确认", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreConfirm = false }) { Text("取消") }
            }
        )
    }

    Scaffold(
        topBar = { WeiboTopBar("设置") }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Cookie ────────────────────────────────────────────
            SectionTitle("登录 Cookie")

            OutlinedTextField(
                value = state.cookieInput,
                onValueChange = vm::onCookieInputChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("微博 Cookie") },
                placeholder = { Text("从 m.weibo.cn 开发者工具中复制") },
                minLines = 3, maxLines = 6,
                trailingIcon = {
                    if (state.cookie.isNotEmpty()) {
                        IconButton(onClick = vm::clearCookie) {
                            Icon(Icons.Filled.Delete, contentDescription = "清除")
                        }
                    }
                }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val hasCookie = state.cookie.isNotEmpty()
                Icon(
                    if (hasCookie) Icons.Filled.CheckCircle else Icons.Filled.Cookie,
                    contentDescription = null,
                    tint = if (hasCookie) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    if (hasCookie) "已配置 Cookie（完整模式）" else "未配置 Cookie（受限模式）",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (hasCookie) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Button(
                onClick = vm::saveCookie,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.cookieInput != state.cookie
            ) { Text(if (state.saved) "已保存" else "保存 Cookie") }

            HorizontalDivider()

            // ── WebDAV ────────────────────────────────────────────
            SectionTitle("WebDAV 备份 / 恢复")

            OutlinedTextField(
                value = state.webDavUrlInput,
                onValueChange = vm::onWebDavUrlChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("服务器地址") },
                placeholder = { Text("https://dav.example.com/dav/weibox") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.webDavUserInput,
                    onValueChange = vm::onWebDavUserChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("用户名") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.webDavPassInput,
                    onValueChange = vm::onWebDavPassChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("密码") },
                    singleLine = true,
                    visualTransformation = if (passVisible) VisualTransformation.None
                                           else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passVisible = !passVisible }) {
                            Icon(
                                if (passVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            Button(
                onClick = vm::saveWebDavConfig,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.webDavUrlInput != state.webDavUrl ||
                          state.webDavUserInput != state.webDavUser ||
                          state.webDavPassInput != state.webDavPass
            ) { Text(if (state.webDavConfigSaved) "已保存" else "保存 WebDAV 配置") }

            // 备份 / 恢复按钮
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = vm::backup,
                    modifier = Modifier.weight(1f),
                    enabled = state.webDavUrl.isNotBlank() && state.webDavOp == WebDavOp.NONE
                ) {
                    if (state.webDavOp == WebDavOp.BACKING_UP) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(6.dp))
                    } else {
                        Icon(Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                    }
                    Text("备份")
                }
                Button(
                    onClick = { showRestoreConfirm = true },
                    modifier = Modifier.weight(1f),
                    enabled = state.webDavUrl.isNotBlank() && state.webDavOp == WebDavOp.NONE
                ) {
                    if (state.webDavOp == WebDavOp.RESTORING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(6.dp))
                    } else {
                        Icon(Icons.Filled.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                    }
                    Text("恢复")
                }
            }

            // 操作结果提示
            state.webDavMessage?.let { msg ->
                val isError = msg.startsWith("备份失败") || msg.startsWith("恢复失败")
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    color = if (isError) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isError) MaterialTheme.colorScheme.onErrorContainer
                                else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            HorizontalDivider()

            // ── 外观 ──────────────────────────────────────────────
            SectionTitle("外观")
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DarkMode, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text("深色模式", style = MaterialTheme.typography.bodyLarge)
                }
                Switch(checked = state.darkMode, onCheckedChange = { vm.toggleDarkMode() })
            }

            HorizontalDivider()

            // ── 支持开发者 ────────────────────────────────────────
            SectionTitle("支持开发者")
            Image(
                painter = painterResource(R.drawable.payme),
                contentDescription = "收款二维码",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedButton(
                onClick = vm::savePayQrCode,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.SaveAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("保存二维码到相册")
            }
            state.donateMessage?.let { msg ->
                val isError = msg.startsWith("保存失败")
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    color = if (isError) MaterialTheme.colorScheme.errorContainer
                            else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = msg,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isError) MaterialTheme.colorScheme.onErrorContainer
                                else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            HorizontalDivider()

            // ── 关于 ──────────────────────────────────────────────
            SectionTitle("关于")
            Text(
                "WeiboX v1.0\n基于 weibo-crawler 数据方案",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}
