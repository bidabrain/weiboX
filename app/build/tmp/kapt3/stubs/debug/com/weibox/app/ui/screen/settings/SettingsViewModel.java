package com.weibox.app.ui.screen.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0011J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u0006\u0010\u001a\u001a\u00020\u0011J\u0006\u0010\u001b\u001a\u00020\u0011J\u0006\u0010\u001c\u001a\u00020\u0011J\u0006\u0010\u001d\u001a\u00020\u0011J\u0006\u0010\u001e\u001a\u00020\u0011R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001f"}, d2 = {"Lcom/weibox/app/ui/screen/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "prefs", "Lcom/weibox/app/data/prefs/AppPreferences;", "repo", "Lcom/weibox/app/data/repository/WeiboRepository;", "context", "Landroid/content/Context;", "(Lcom/weibox/app/data/prefs/AppPreferences;Lcom/weibox/app/data/repository/WeiboRepository;Landroid/content/Context;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/weibox/app/ui/screen/settings/SettingsUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "backup", "Lkotlinx/coroutines/Job;", "clearCookie", "onCookieInputChange", "", "v", "", "onWebDavPassChange", "onWebDavUrlChange", "onWebDavUserChange", "restore", "saveCookie", "savePayQrCode", "saveWebDavConfig", "toggleDarkMode", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.data.prefs.AppPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.data.repository.WeiboRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.weibox.app.ui.screen.settings.SettingsUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.weibox.app.ui.screen.settings.SettingsUiState> state = null;
    
    @javax.inject.Inject()
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.weibox.app.data.prefs.AppPreferences prefs, @org.jetbrains.annotations.NotNull()
    com.weibox.app.data.repository.WeiboRepository repo, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.weibox.app.ui.screen.settings.SettingsUiState> getState() {
        return null;
    }
    
    public final void onCookieInputChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job saveCookie() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job clearCookie() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job toggleDarkMode() {
        return null;
    }
    
    public final void onWebDavUrlChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onWebDavUserChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onWebDavPassChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job saveWebDavConfig() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job backup() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job savePayQrCode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job restore() {
        return null;
    }
}