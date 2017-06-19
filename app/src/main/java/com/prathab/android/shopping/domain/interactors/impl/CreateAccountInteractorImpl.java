package com.prathab.android.shopping.domain.interactors.impl;

import com.prathab.android.shopping.domain.interactors.CreateAccountInteractor;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import io.reactivex.Single;

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

    return Single.create(e -> mCreateAccountRepository.createAccount(
        new CreateAccountRepositoryRequest(name, mobile, password))
        .subscribe(createAccountRepositoryResponse -> e.onSuccess("Successfully Created Account"),
            throwable -> {

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
            }));
  }
}