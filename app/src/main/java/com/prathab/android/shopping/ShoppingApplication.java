package com.prathab.android.shopping;

import android.app.Application;
import com.prathab.android.shopping.dagger.AppComponent;
import com.prathab.android.shopping.dagger.AppModule;
import com.prathab.android.shopping.dagger.DaggerAppComponent;

/**
 * Application class
 */
public class ShoppingApplication extends Application {
  AppComponent appComponent;

  @Override public void onCreate() {
    super.onCreate();
    appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    appComponent.inject(this);
  }

  public AppComponent getAppComponent() {
    return appComponent;
  }
}
