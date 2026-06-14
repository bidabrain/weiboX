package com.weibox.app.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000e\b\u0007\u0018\u0000 12\u00020\u0001:\u00011B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J@\u0010\u000b\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\u0006\u0012\u0004\u0018\u00010\n0\f2\u0006\u0010\u000f\u001a\u00020\n2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J$\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\r2\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u001dJ\u0012\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\r0\u001fJ\u001a\u0010!\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\r0\u001f2\u0006\u0010\u0016\u001a\u00020\nJ\u0012\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\r0\u001fJ\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020$0\u001f2\u0006\u0010\u0016\u001a\u00020\nJ\u001c\u0010%\u001a\b\u0012\u0004\u0012\u00020 0\r2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010&J$\u0010\'\u001a\b\u0012\u0004\u0012\u00020 0\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010(\u001a\u00020$H\u0082@\u00a2\u0006\u0002\u0010)J\u0014\u0010*\u001a\b\u0012\u0004\u0012\u00020 0\rH\u0086@\u00a2\u0006\u0002\u0010+J&\u0010,\u001a\b\u0012\u0004\u0012\u00020 0\r2\u0006\u0010\u0016\u001a\u00020\n2\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0017J&\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00150\r2\u0006\u0010.\u001a\u00020\n2\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010/\u001a\u00020\u001bH\u0082@\u00a2\u0006\u0002\u0010+J\u0016\u00100\u001a\u00020\u001b2\u0006\u0010\u0016\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00062"}, d2 = {"Lcom/weibox/app/data/repository/WeiboRepository;", "", "db", "Lcom/weibox/app/data/db/AppDatabase;", "prefs", "Lcom/weibox/app/data/prefs/AppPreferences;", "(Lcom/weibox/app/data/db/AppDatabase;Lcom/weibox/app/data/prefs/AppPreferences;)V", "api", "Lcom/weibox/app/data/api/WeiboApi;", "cookie", "", "fetchComments", "Lkotlin/Pair;", "", "Lcom/weibox/app/data/model/WeiboComment;", "postId", "maxId", "page", "", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchFollowingList", "Lcom/weibox/app/data/model/WeiboUser;", "userId", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchUser", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "followUser", "", "user", "(Lcom/weibox/app/data/model/WeiboUser;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCachedTimeline", "Lkotlinx/coroutines/flow/Flow;", "Lcom/weibox/app/data/model/WeiboPost;", "getCachedUserPosts", "getFollowedUsers", "isFollowed", "", "loadMoreTimeline", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadTimelinePage", "replace", "(IZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshTimeline", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshUserPosts", "searchUsers", "query", "trimCache", "unfollowUser", "Companion", "app_debug"})
public final class WeiboRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.data.db.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.data.prefs.AppPreferences prefs = null;
    private static final int MAX_CACHED_POSTS = 500;
    @org.jetbrains.annotations.NotNull()
    public static final com.weibox.app.data.repository.WeiboRepository.Companion Companion = null;
    
    @javax.inject.Inject()
    public WeiboRepository(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.db.AppDatabase db, @org.jetbrains.annotations.NotNull()
    com.weibox.app.data.prefs.AppPreferences prefs) {
        super();
    }
    
    private final com.weibox.app.data.api.WeiboApi api(java.lang.String cookie) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.weibox.app.data.model.WeiboUser>> getFollowedUsers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isFollowed(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object followUser(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.model.WeiboUser user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object unfollowUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchUser(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.weibox.app.data.model.WeiboUser> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchFollowingList(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboUser>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchComments(@org.jetbrains.annotations.NotNull()
    java.lang.String postId, @org.jetbrains.annotations.Nullable()
    java.lang.String maxId, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<? extends java.util.List<com.weibox.app.data.model.WeiboComment>, java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchUsers(@org.jetbrains.annotations.NotNull()
    java.lang.String query, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboUser>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.weibox.app.data.model.WeiboPost>> getCachedTimeline() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.weibox.app.data.model.WeiboPost>> getCachedUserPosts(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshTimeline(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboPost>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadMoreTimeline(int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboPost>> $completion) {
        return null;
    }
    
    private final java.lang.Object loadTimelinePage(int page, boolean replace, kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboPost>> $completion) {
        return null;
    }
    
    private final java.lang.Object trimCache(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refreshUserPosts(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, int page, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.weibox.app.data.model.WeiboPost>> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/weibox/app/data/repository/WeiboRepository$Companion;", "", "()V", "MAX_CACHED_POSTS", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}