# WeiboX

一款基于 Jetpack Compose 的第三方微博 Android 客户端，无需注册账号，通过粘贴 Cookie 即可浏览关注用户的微博时间线。

## 功能

- **时间线**：聚合所有本地关注用户的最新微博，下拉刷新，滑到底部自动加载更多
- **内容发现**：按昵称或数字 ID 搜索用户，直接关注
- **关注管理**：本地维护关注列表（独立于微博账号），支持随时取关
- **用户主页**：头图 / 头像 / 简介 / 统计数据 / 微博列表 / 关注列表
- **评论**：查看微博评论（需 Cookie 解锁热门评论分页）
- **WebDAV 备份 / 恢复**：将关注列表和 Cookie 备份到私有 WebDAV 服务器
- **深色模式**：跟随系统或手动切换

## 技术栈

| 层 | 技术 |
|---|---|
| UI | Jetpack Compose + Material3 |
| 架构 | MVVM + Hilt 依赖注入 |
| 网络 | OkHttp（直连 `m.weibo.cn` 移动端接口） |
| 本地缓存 | Room（帖子缓存 500 条 / 7 天自动清理） |
| 持久化 | DataStore Preferences |
| 图片 | Coil |

## 界面预览

![UI 预览](docs/ui_mockup.png)

> 通过 `python3 generate_mockup.py` 可重新生成此图（需要 Pillow ≥ 8.2）。

## 致谢

本项目的网络请求逻辑、接口地址、请求头策略以及数据解析方案，均参考自开源项目：

**[dataabc/weibo-crawler](https://github.com/dataabc/weibo-crawler)**

WeiboX 可以理解为将 weibo-crawler 的 Python 数据获取层，用 Kotlin 为 Android 平台重新实现的版本。感谢原项目作者的持续维护。

---

## 项目结构

```
weiboX/
├── app/src/main/
│   ├── AndroidManifest.xml
│   └── java/com/weibox/app/
│       ├── MainActivity.kt              # 入口，主题注入
│       ├── WeiboXApp.kt                 # Application，Hilt 初始化
│       │
│       ├── data/
│       │   ├── api/
│       │   │   └── WeiboApi.kt          # ★ 核心：所有 m.weibo.cn 接口调用
│       │   ├── db/
│       │   │   ├── AppDatabase.kt
│       │   │   ├── dao/
│       │   │   │   ├── PostDao.kt
│       │   │   │   └── UserDao.kt
│       │   │   └── entity/
│       │   │       ├── PostEntity.kt
│       │   │       └── UserEntity.kt
│       │   ├── model/
│       │   │   ├── WeiboComment.kt
│       │   │   ├── WeiboPost.kt
│       │   │   └── WeiboUser.kt
│       │   ├── prefs/
│       │   │   └── AppPreferences.kt    # Cookie / WebDAV / 深色模式持久化
│       │   ├── repository/
│       │   │   └── WeiboRepository.kt   # 数据层统一入口
│       │   └── webdav/
│       │       └── WebDavService.kt     # WebDAV 备份 / 恢复
│       │
│       ├── di/
│       │   └── AppModule.kt             # Hilt 模块
│       │
│       ├── navigation/
│       │   └── NavGraph.kt              # 底部导航 + 路由
│       │
│       ├── ui/
│       │   ├── components/
│       │   │   ├── CommentsBottomSheet.kt
│       │   │   ├── ImageViewer.kt
│       │   │   ├── PostCard.kt
│       │   │   ├── UserCard.kt
│       │   │   └── WeiboTopBar.kt       # 统一顶部导航栏
│       │   ├── screen/
│       │   │   ├── following/           # 关注 Tab
│       │   │   ├── followinglist/       # 某用户的关注列表
│       │   │   ├── home/                # 时间线 Tab
│       │   │   ├── profile/             # 用户主页
│       │   │   ├── search/              # 搜索 Tab
│       │   │   └── settings/            # 设置 Tab
│       │   └── theme/
│       │       ├── Color.kt
│       │       ├── Theme.kt
│       │       └── Type.kt
│       └── ...
└── app/src/main/res/
    └── drawable/
        └── payme.jpg                    # 支持开发者收款二维码
```

---

## 接口对照表（WeiboApi.kt ↔ weibo-crawler/weibo.py）

当 weibo-crawler 更新时，优先检查下表中对应的行号范围。

| WeiboApi.kt 方法 | 功能 | weibo.py 对应位置 |
|---|---|---|
| `getUserInfo()` | 获取用户信息 | `get_user_info()` **L781**，containerid `100505{uid}` **L783** |
| `getUserPosts()` | 获取用户微博列表 | `get_weibo_json()` **L606**，containerid `230413{uid}` **L616** |
| `getFollowingList()` | 获取用户的关注列表 | 无独立函数，containerid `231051_-_followers_-_{uid}` |
| `getComments()`（有 Cookie） | 热门评论分页 | `_get_weibo_comments_cookie()` **L1712**，URL `hotflow?max_id_type=0` **L1729** |
| `getComments()`（无 Cookie） | 评论基础接口 | `_get_weibo_comments_nocookie()` **L1778**，URL `comments/show` **L1791** |
| `searchUsers()` | 搜索用户 | `get_weibo_json()` **L606**，containerid `100103type=3&q=` **L612** |
| `parsePost()` | 解析单条微博字段 | `parse_weibo()` **L1536**，字段 `attitudes_count` **L1568** / `comments_count` **L1571** / `reposts_count` **L1574** / `created_at` **L1566** / `source` **L1567** |
| `parsePics()` | 解析图片列表 | `get_pics()` **L922**，`pic['large']['url']` **L933** |
| `stripHtml()` | 去除正文 HTML 标签 | `remove_html_tag` 配置 **L57**，`re.sub` 处理 |
| Cookie / XSRF 初始化 | 解析并注入鉴权信息 | Cookie 清洗 **L150**，XSRF-TOKEN 提取 **L162** |
| 请求头（UA / Referer / MWeibo-Pwa） | 模拟移动端浏览器 | User-Agent 随机选择 **L360**，Referer `m.weibo.cn` **L174** |

---

## 根据 weibo-crawler 更新的维护方法

### 何时需要同步

微博偶尔会调整接口路径、返回字段或鉴权机制。weibo-crawler 作为活跃维护的项目，通常会率先修复。当出现以下情况时，参照本流程同步：

- WeiboX 请求返回 `ok=-100` 或触发验证码
- 某功能数据为空而 weibo-crawler 正常
- weibo-crawler 发布了涉及 `WeiboApi.kt` 对照范围内的 commit

### 同步流程

**第一步：定位 weibo-crawler 的改动**

```bash
cd weibo-crawler
git log --oneline -20          # 查看最近提交
git diff HEAD~1 HEAD weibo.py  # 查看最新一次对 weibo.py 的改动
```

**第二步：根据对照表找到 WeiboApi.kt 的对应位置**

对照上表，确认改动影响哪个方法，直接跳转到 `WeiboApi.kt` 中对应的函数。

**第三步：逐项移植**

常见改动类型及处理方式：

| 改动类型 | weibo.py 常见位置 | WeiboApi.kt 处理位置 |
|---|---|---|
| 接口 URL / containerid 变更 | `get_weibo_json()` / `get_user_info()` | 对应 `suspend fun` 中的 `url` 字符串 |
| 请求头字段增删 | `__init__` headers / `get_json()` | `OkHttpClient` 拦截器中的 `.header(...)` |
| Cookie / XSRF 鉴权逻辑 | L150–L165 | `cookieJar` 初始化 + `getXsrfToken()` |
| 返回 JSON 字段改名 | `parse_weibo()` L1536 | `parsePost()` 中的 `optString / optInt` |
| 图片字段结构变更 | `get_pics()` L922 | `parsePics()` |
| 评论接口参数变更 | `_get_weibo_comments_cookie()` L1712 | `getComments()` 中的 URL 拼接 |

**第四步：验证**

1. 构建并安装到真机
2. 打开时间线，检查帖子是否正常加载
3. 点击评论，确认有 Cookie 时热门评论可分页
4. 搜索用户，确认结果正常返回
