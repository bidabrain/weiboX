package com.weibox.app;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = WeiboXApp.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface WeiboXApp_GeneratedInjector {
  void injectWeiboXApp(WeiboXApp weiboXApp);
}
