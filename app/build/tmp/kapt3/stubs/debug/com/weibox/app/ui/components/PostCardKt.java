package com.weibox.app.ui.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000N\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0000\u001a*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0010\b\u0002\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\nH\u0003\u001a4\u0010\u000b\u001a\u00020\u00042\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\b0\u00012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00040\u000e2\b\b\u0002\u0010\u0010\u001a\u00020\u000fH\u0003\u001a:\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00132\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00040\u000e2\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0007\u001a8\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00132\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00040\u000e2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00040\u000eH\u0003\u001a4\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\b2\b\b\u0002\u0010\u001d\u001a\u00020\u000f2\u0010\b\u0002\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\nH\u0007\u001a\u0010\u0010\u001e\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020\u000fH\u0002\u001a\u0010\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\"H\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"avatarColors", "", "Landroidx/compose/ui/graphics/Color;", "ActionItem", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "", "onClick", "Lkotlin/Function0;", "ImageGrid", "pics", "onImageClick", "Lkotlin/Function1;", "", "maxSize", "PostCard", "post", "Lcom/weibox/app/data/model/WeiboPost;", "onUserClick", "repo", "Lcom/weibox/app/data/repository/WeiboRepository;", "modifier", "Landroidx/compose/ui/Modifier;", "RetweetCard", "UserAvatar", "avatarUrl", "name", "size", "formatCount", "n", "formatTime", "ts", "", "app_debug"})
public final class PostCardKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<androidx.compose.ui.graphics.Color> avatarColors = null;
    
    @androidx.compose.runtime.Composable()
    public static final void PostCard(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.model.WeiboPost post, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onUserClick, @org.jetbrains.annotations.Nullable()
    com.weibox.app.data.repository.WeiboRepository repo, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void UserAvatar(@org.jetbrains.annotations.NotNull()
    java.lang.String avatarUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String name, int size, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void RetweetCard(com.weibox.app.data.model.WeiboPost post, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onUserClick, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onImageClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ImageGrid(java.util.List<java.lang.String> pics, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onImageClick, int maxSize) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ActionItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String formatCount(int n) {
        return null;
    }
    
    private static final java.lang.String formatTime(long ts) {
        return null;
    }
}