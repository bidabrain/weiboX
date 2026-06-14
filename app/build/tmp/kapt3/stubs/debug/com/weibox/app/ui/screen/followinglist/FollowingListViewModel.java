package com.weibox.app.ui.screen.followinglist;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rJ\u0010\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0006\u0010\u0014\u001a\u00020\u000fJ\u000e\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0016\u001a\u00020\u0017R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/weibox/app/ui/screen/followinglist/FollowingListViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/weibox/app/data/repository/WeiboRepository;", "(Lcom/weibox/app/data/repository/WeiboRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/weibox/app/ui/screen/followinglist/FollowingListUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "userId", "", "init", "", "uid", "load", "page", "", "loadMore", "toggleFollow", "user", "Lcom/weibox/app/data/model/WeiboUser;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class FollowingListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.data.repository.WeiboRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.weibox.app.ui.screen.followinglist.FollowingListUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.weibox.app.ui.screen.followinglist.FollowingListUiState> state = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String userId = "";
    
    @javax.inject.Inject()
    public FollowingListViewModel(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.repository.WeiboRepository repo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.weibox.app.ui.screen.followinglist.FollowingListUiState> getState() {
        return null;
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    java.lang.String uid) {
    }
    
    private final void load(int page) {
    }
    
    public final void loadMore() {
    }
    
    public final void toggleFollow(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.model.WeiboUser user) {
    }
}