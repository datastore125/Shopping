package com.prathab.android.shopping.storage;

import android.content.SharedPreferences;
import com.prathab.android.shopping.constants.HttpConstants;
import com.prathab.android.shopping.constants.SharedPreferencesConstants;
import com.prathab.android.shopping.domain.model.Users;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import com.prathab.android.shopping.network.AccountsClient;
import com.prathab.android.shopping.network.services.AccountsService;
import com.prathab.android.shopping.utility.Validators;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

/**
 * Concrete implementation of {@link CreateAccountRepository}
 */
public class CreateAccountRepositoryImpl implements CreateAccountRepository {
  SharedPreferences mSharedPreferences;

  public CreateAccountRepositoryImpl(SharedPreferences sharedPreferences) {
    this.mSharedPreferences = sharedPreferences;
  }

  @Override public Single<CreateAccountRepositoryResponse> createAccount(
      final CreateAccountRepositoryRequest createAccountRepositoryRequest) {

    return Single.create(e -> {

      if (createAccountRepositoryRequest.getName().isEmpty() || !Validators.isMobileValid(
          createAccountRepositoryRequest.getMobile()) || !Validators.isPasswordValid(
          createAccountRepositoryRequest.getPassword())) {

        //Error code of 1 corresponds to Invalid input detected locally
        e.onError(new CreateAccountRepositoryException("1"));
        return;
      }

      Users createAccountUsersRequest =
          new Users.Builder().setName(createAccountRepositoryRequest.getName())
              .setMobile(createAccountRepositoryRequest.getMobile())
              .setPassword(createAccountRepositoryRequest.getPassword())
              .build();

      AccountsService accountsService = AccountsClient.getClient().create(AccountsService.class);

      Observable<Response<Users>> responseObservable =
          accountsService.createAccount(createAccountUsersRequest);

      responseObservable.subscribe(usersResponse -> {
        if (usersResponse.code() == 200) {

          String authToken = usersResponse.headers().get(HttpConstants.HTTP_HEADER_JWT_TOKEN);

          mSharedPreferences.edit()
              .putString(SharedPreferencesConstants.JWT_TOKEN, authToken)
              .apply();

          e.onSuccess(new CreateAccountRepositoryResponse(authToken));
          return;
        }
        e.onError(new CreateAccountRepositoryException(String.valueOf(usersResponse.code())));
      }, throwable -> e.onError(new CreateAccountRepositoryException(throwable.getMessage())));
    });
  }
}
