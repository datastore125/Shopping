package com.prathab.android.shopping.dagger;

import android.content.Context;
import com.prathab.android.shopping.ShoppingApplication;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import com.prathab.android.shopping.domain.repository.LoginRepository;
import com.prathab.android.shopping.storage.CreateAccountRepositoryImpl;
import com.prathab.android.shopping.storage.LoginRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;
import static com.prathab.android.shopping.constants.SharedPreferencesConstants.SHARED_PREFERENCES_NAME;

/**
 * Dagger Module class
 */
@Module public class AppModule {
  private final ShoppingApplication application;

  public AppModule(ShoppingApplication application) {
    this.application = application;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return application;
  }

  @Provides @Singleton ShoppingApplication provideShoppingApplication() {
    return application;
  }

  @Provides @Inject LoginRepository provideLoginRepository(Context context) {
    return new LoginRepositoryImpl(
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE));
  }

  @Provides @Inject CreateAccountRepository provideCreateAccountRepository(Context context) {
    return new CreateAccountRepositoryImpl(
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE));
  }
}
