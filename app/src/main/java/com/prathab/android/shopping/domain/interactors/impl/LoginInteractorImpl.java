package com.prathab.android.shopping.domain.interactors.impl;

import com.prathab.android.shopping.domain.interactors.LoginInteractor;
import com.prathab.android.shopping.domain.repository.LoginRepository;
import com.prathab.android.shopping.domain.repository.LoginRepository.LoginRepositoryRequest;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.prathab.android.shopping.domain.repository.LoginRepository.LoginRepositoryResponse;

/**
 * Concrete Implementation of {@link LoginInteractor}
 * This interactor handles login using a {@link LoginRepository}
 */
public class LoginInteractorImpl implements LoginInteractor {
  private LoginRepository mLoginRepository;

  public LoginInteractorImpl(LoginRepository loginRepository) {
    this.mLoginRepository = loginRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override public Single<String> login(final String mobile, final String password) {
    return Single.create(new SingleOnSubscribe<String>() {
      @Override public void subscribe(@NonNull final SingleEmitter<String> e) throws Exception {

        mLoginRepository.login(new LoginRepositoryRequest(mobile, password))
            .subscribe(new Consumer<LoginRepository.LoginRepositoryResponse>() {
              @Override public void accept(@NonNull LoginRepositoryResponse loginRepositoryResponse)
                  throws Exception {
                e.onSuccess("Successfully Logged In");
              }
            }, new Consumer<Throwable>() {
              @Override public void accept(@NonNull Throwable throwable) throws Exception {

                switch (throwable.getMessage()) {
                  case "1":
                    //Represents Invalid input detected without network call
                    e.onError(new LoginInteractorException("Enter Valid Details"));
                    break;
                  case "401":
                    e.onError(new LoginInteractorException("Check your details"));
                    break;
                  case "404":
                    e.onError(new LoginInteractorException("Please try again later"));
                    break;
                  default:
                    e.onError(new LoginInteractorException("Unexpected Error"));
                    break;
                }
              }
            });
      }
    });
  }
}