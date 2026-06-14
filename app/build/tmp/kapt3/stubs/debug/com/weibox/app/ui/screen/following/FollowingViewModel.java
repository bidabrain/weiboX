package com.weibox.app.ui.screen.following;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lcom/weibox/app/ui/screen/following/FollowingViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/weibox/app/data/repository/WeiboRepository;", "(Lcom/weibox/app/data/repository/WeiboRepository;)V", "users", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/weibox/app/data/model/WeiboUser;", "getUsers", "()Lkotlinx/coroutines/flow/StateFlow;", "unfollow", "", "userId", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FollowingViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.data.repository.WeiboRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.weibox.app.data.model.WeiboUser>> users = null;
    
    @javax.inject.Inject()
    public FollowingViewModel(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.repository.WeiboRepository repo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.weibox.app.data.model.WeiboUser>> getUsers() {
        return null;
    }
    
    public final void unfollow(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
}