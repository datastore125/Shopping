package com.prathab.android.shopping.storage;

import android.content.SharedPreferences;
import com.prathab.android.shopping.constants.HttpConstants;
import com.prathab.android.shopping.constants.SharedPreferencesConstants;
import com.prathab.android.shopping.domain.model.Users;
import com.prathab.android.shopping.domain.repository.LoginRepository;
import com.prathab.android.shopping.network.AccountsClient;
import com.prathab.android.shopping.network.services.AccountsService;
import com.prathab.android.shopping.utility.Validators;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

/**
 * Concrete implementation of {@link LoginRepository}
 */
public class LoginRepositoryImpl implements LoginRepository {
  SharedPreferences mSharedPreferences;

  public LoginRepositoryImpl(SharedPreferences sharedPreferences) {
    this.mSharedPreferences = sharedPreferences;
  }

  @Override public Single<LoginRepositoryResponse> login(
      final LoginRepositoryRequest loginRepositoryRequest) {

    return Single.create(e -> {

      if (!Validators.isMobileValid(loginRepositoryRequest.getMobile())
          || !Validators.isPasswordValid(loginRepositoryRequest.getPassword())) {

        //Error code of 1 corresponds to Invalid input detected locally
        e.onError(new LoginRepositoryException("1"));
        return;
      }

      Users loginUsersRequest = new Users.Builder().setMobile(loginRepositoryRequest.getMobile())
          .setPassword(loginRepositoryRequest.getPassword())
          .build();

      AccountsService accountsService = AccountsClient.getClient().create(AccountsService.class);

      Observable<Response<Users>> responseObservable = accountsService.login(loginUsersRequest);

      responseObservable.subscribe(usersResponse -> {
        if (usersResponse.code() == 200) {

          String authToken = usersResponse.headers().get(HttpConstants.HTTP_HEADER_JWT_TOKEN);

          mSharedPreferences.edit()
              .putString(SharedPreferencesConstants.JWT_TOKEN, authToken)
              .apply();

          e.onSuccess(new LoginRepositoryResponse(authToken));
          return;
        }
        e.onError(new LoginRepositoryException(String.valueOf(usersResponse.code())));
      }, throwable -> e.onError(new LoginRepositoryException(throwable.getMessage())));
    });
  }
}
