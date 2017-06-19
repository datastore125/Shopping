package com.prathab.android.shopping.domain.interactors.impl;

import com.prathab.android.shopping.domain.interactors.CreateAccountInteractor;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.prathab.android.shopping.domain.repository.CreateAccountRepository.CreateAccountRepositoryException;
import static com.prathab.android.shopping.domain.repository.CreateAccountRepository.CreateAccountRepositoryRequest;

/**
 * Concrete Implementation of {@link CreateAccountInteractor}
 * This interactor handles createAccount using a {@link CreateAccountRepository}
 */
public class CreateAccountInteractorImpl implements CreateAccountInteractor {
  private CreateAccountRepository mCreateAccountRepository;

  public CreateAccountInteractorImpl(CreateAccountRepository createAccountRepository) {
    this.mCreateAccountRepository = createAccountRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override public Single<String> execute(final String name, final String mobile,
      final String password) {

    return Single.create(new SingleOnSubscribe<String>() {
      @Override public void subscribe(@NonNull final SingleEmitter<String> e) throws Exception {

        mCreateAccountRepository.createAccount(
            new CreateAccountRepositoryRequest(name, mobile, password))
            .subscribe(new Consumer<CreateAccountRepository.CreateAccountRepositoryResponse>() {
              @Override public void accept(@NonNull
                  CreateAccountRepository.CreateAccountRepositoryResponse createAccountRepositoryResponse)
                  throws Exception {
                e.onSuccess("Successfully Created Account");
              }
            }, new Consumer<Throwable>() {
              @Override public void accept(@NonNull Throwable throwable) throws Exception {

                switch (throwable.getMessage()) {
                  case "1":
                    //Represents Invalid input detected without network call
                    e.onError(new CreateAccountRepositoryException("Enter Valid Details"));
                    break;
                  case "400":
                    e.onError(new CreateAccountRepositoryException("Check your detail"));
                    break;
                  case "409":
                    e.onError(new CreateAccountRepositoryException("Account Already Exists"));
                    break;
                  case "404":
                    e.onError(new CreateAccountRepositoryException("Please try again later"));
                    break;
                  default:
                    e.onError(new CreateAccountRepositoryException("Unexpected Error"));
                    break;
                }
              }
            });
      }
    });
  }
}