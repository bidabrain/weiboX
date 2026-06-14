package com.weibox.app.ui.screen.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b&\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0013J\t\u0010%\u001a\u00020\u0003H\u00c6\u0003J\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0006H\u00c6\u0003J\t\u0010(\u001a\u00020\u0010H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u0006H\u00c6\u0003J\t\u0010-\u001a\u00020\u0006H\u00c6\u0003J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\u0099\u0001\u00103\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u00104\u001a\u00020\u00062\b\u00105\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00106\u001a\u000207H\u00d6\u0001J\t\u00108\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0015R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0011\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0015R\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0015R\u0011\u0010\r\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0015R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0015R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0015R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0015R\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0015\u00a8\u00069"}, d2 = {"Lcom/weibox/app/ui/screen/settings/SettingsUiState;", "", "cookie", "", "cookieInput", "darkMode", "", "saved", "webDavUrl", "webDavUser", "webDavPass", "webDavUrlInput", "webDavUserInput", "webDavPassInput", "webDavConfigSaved", "webDavOp", "Lcom/weibox/app/ui/screen/settings/WebDavOp;", "webDavMessage", "donateMessage", "(Ljava/lang/String;Ljava/lang/String;ZZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/weibox/app/ui/screen/settings/WebDavOp;Ljava/lang/String;Ljava/lang/String;)V", "getCookie", "()Ljava/lang/String;", "getCookieInput", "getDarkMode", "()Z", "getDonateMessage", "getSaved", "getWebDavConfigSaved", "getWebDavMessage", "getWebDavOp", "()Lcom/weibox/app/ui/screen/settings/WebDavOp;", "getWebDavPass", "getWebDavPassInput", "getWebDavUrl", "getWebDavUrlInput", "getWebDavUser", "getWebDavUserInput", "component1", "component10", "component11", "component12", "component13", "component14", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class SettingsUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cookie = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String cookieInput = null;
    private final boolean darkMode = false;
    private final boolean saved = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webDavUrl = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webDavUser = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webDavPass = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webDavUrlInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webDavUserInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String webDavPassInput = null;
    private final boolean webDavConfigSaved = false;
    @org.jetbrains.annotations.NotNull()
    private final com.weibox.app.ui.screen.settings.WebDavOp webDavOp = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String webDavMessage = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String donateMessage = null;
    
    public SettingsUiState(@org.jetbrains.annotations.NotNull()
    java.lang.String cookie, @org.jetbrains.annotations.NotNull()
    java.lang.String cookieInput, boolean darkMode, boolean saved, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUser, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavPass, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUrlInput, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUserInput, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavPassInput, boolean webDavConfigSaved, @org.jetbrains.annotations.NotNull()
    com.weibox.app.ui.screen.settings.WebDavOp webDavOp, @org.jetbrains.annotations.Nullable()
    java.lang.String webDavMessage, @org.jetbrains.annotations.Nullable()
    java.lang.String donateMessage) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCookie() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCookieInput() {
        return null;
    }
    
    public final boolean getDarkMode() {
        return false;
    }
    
    public final boolean getSaved() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebDavUrl() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebDavUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebDavPass() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebDavUrlInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebDavUserInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getWebDavPassInput() {
        return null;
    }
    
    public final boolean getWebDavConfigSaved() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.weibox.app.ui.screen.settings.WebDavOp getWebDavOp() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getWebDavMessage() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getDonateMessage() {
        return null;
    }
    
    public SettingsUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.weibox.app.ui.screen.settings.WebDavOp component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component14() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.weibox.app.ui.screen.settings.SettingsUiState copy(@org.jetbrains.annotations.NotNull()
    java.lang.String cookie, @org.jetbrains.annotations.NotNull()
    java.lang.String cookieInput, boolean darkMode, boolean saved, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUser, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavPass, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUrlInput, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavUserInput, @org.jetbrains.annotations.NotNull()
    java.lang.String webDavPassInput, boolean webDavConfigSaved, @org.jetbrains.annotations.NotNull()
    com.weibox.app.ui.screen.settings.WebDavOp webDavOp, @org.jetbrains.annotations.Nullable()
    java.lang.String webDavMessage, @org.jetbrains.annotations.Nullable()
    java.lang.String donateMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}