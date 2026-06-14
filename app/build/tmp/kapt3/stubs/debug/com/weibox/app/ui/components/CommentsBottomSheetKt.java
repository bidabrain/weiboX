package com.weibox.app.ui.components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a.\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000fH\u0002\u00a8\u0006\u0010"}, d2 = {"CommentItem", "", "comment", "Lcom/weibox/app/data/model/WeiboComment;", "CommentsBottomSheet", "postId", "", "commentCount", "", "repo", "Lcom/weibox/app/data/repository/WeiboRepository;", "onDismiss", "Lkotlin/Function0;", "formatCommentTime", "ts", "", "app_debug"})
public final class CommentsBottomSheetKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void CommentsBottomSheet(@org.jetbrains.annotations.NotNull()
    java.lang.String postId, int commentCount, @org.jetbrains.annotations.NotNull()
    com.weibox.app.data.repository.WeiboRepository repo, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CommentItem(com.weibox.app.data.model.WeiboComment comment) {
    }
    
    private static final java.lang.String formatCommentTime(long ts) {
        return null;
    }
}