package com.prathab.android.shopping.dagger;

import com.prathab.android.shopping.ShoppingApplication;
import com.prathab.android.shopping.presentation.ui.activities.CreateAccount;
import com.prathab.android.shopping.presentation.ui.activities.Login;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Dagger Component interface
 */
@Singleton @Component(modules = { AppModule.class }) public interface AppComponent {
  void inject(ShoppingApplication application);

  void inject(Login login);

  void inject(CreateAccount createAccount);
}
