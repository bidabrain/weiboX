package com.weibox.app.data.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003H\u0002J@\u0010\u000f\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00102\u0006\u0010\u0013\u001a\u00020\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0017J$\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u00112\u0006\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0016\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0003H\u0086@\u00a2\u0006\u0002\u0010\u001dJ$\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00112\u0006\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001bJ,\u0010 \u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u00102\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020#H\u0002J\u0018\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00030\u00112\b\u0010%\u001a\u0004\u0018\u00010&H\u0002J\u000e\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\fJ\u0016\u0010)\u001a\b\u0012\u0004\u0012\u00020\u001f0\u00112\u0006\u0010!\u001a\u00020\u0003H\u0002J\u0010\u0010*\u001a\u00020\u00192\u0006\u0010!\u001a\u00020\u0003H\u0002J\u0010\u0010+\u001a\u00020\u00192\u0006\u0010,\u001a\u00020\fH\u0002J\u0016\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00190\u00112\u0006\u0010!\u001a\u00020\u0003H\u0002J&\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00190\u00112\u0006\u0010/\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0010\u00100\u001a\u00020\u00032\u0006\u00101\u001a\u00020\u0003H\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lcom/weibox/app/data/api/WeiboApi;", "", "cookieString", "", "(Ljava/lang/String;)V", "client", "Lokhttp3/OkHttpClient;", "cookieJar", "Lokhttp3/CookieJar;", "checkOk", "", "root", "Lorg/json/JSONObject;", "get", "url", "getComments", "Lkotlin/Pair;", "", "Lcom/weibox/app/data/model/WeiboComment;", "postId", "maxId", "page", "", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFollowingList", "Lcom/weibox/app/data/model/WeiboUser;", "userId", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserInfo", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUserPosts", "Lcom/weibox/app/data/model/WeiboPost;", "parseComments", "json", "hasCookie", "", "parsePics", "arr", "Lorg/json/JSONArray;", "parsePost", "mblog", "parsePosts", "parseUserInfo", "parseUserJson", "u", "parseUserSearch", "searchUsers", "query", "stripHtml", "html", "app_debug"})
public final class WeiboApi {
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.CookieJar cookieJar = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    
    public WeiboApi(@org.jetbrains.annotations.NotNull()
    java.lang.String cookieString) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUserInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.weibox.app.data.model.WeiboUser> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUserPosts(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboPost>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getFollowingList(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboUser>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getComments(@org.jetbrains.annotations.NotNull()
    java.lang.String postId, @org.jetbrains.annotations.Nullable()
    java.lang.String maxId, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<? extends java.util.List<com.weibox.app.data.model.WeiboComment>, java.lang.String>> $completion) {
        return null;
    }
    
    private final kotlin.Pair<java.util.List<com.weibox.app.data.model.WeiboComment>, java.lang.String> parseComments(java.lang.String json, boolean hasCookie) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchUsers(@org.jetbrains.annotations.NotNull()
    java.lang.String query, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboUser>> $completion) {
        return null;
    }
    
    private final java.util.List<com.weibox.app.data.model.WeiboUser> parseUserSearch(java.lang.String json) {
        return null;
    }
    
    private final com.weibox.app.data.model.WeiboUser parseUserJson(org.json.JSONObject u) {
        return null;
    }
    
    private final java.lang.String get(java.lang.String url) {
        return null;
    }
    
    private final void checkOk(org.json.JSONObject root) {
    }
    
    private final com.weibox.app.data.model.WeiboUser parseUserInfo(java.lang.String json) {
        return null;
    }
    
    private final java.util.List<com.weibox.app.data.model.WeiboPost> parsePosts(java.lang.String json) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.weibox.app.data.model.WeiboPost parsePost(@org.jetbrains.annotations.NotNull()
    org.json.JSONObject mblog) {
        return null;
    }
    
    private final java.util.List<java.lang.String> parsePics(org.json.JSONArray arr) {
        return null;
    }
    
    private final java.lang.String stripHtml(java.lang.String html) {
        return null;
    }
}