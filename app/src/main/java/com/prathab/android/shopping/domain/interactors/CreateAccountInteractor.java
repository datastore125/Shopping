package com.prathab.android.shopping.domain.interactors;

import io.reactivex.Single;
import org.reactivestreams.Subscriber;

public interface CreateAccountInteractor {

  /**
   * @param name Name entered by the user
   * @param mobile Mobile number entered by the user
   * @param password Password entered by the user
   * @return {@link Single} String message which should be displayed in UI will be returned in
   * onSuccess and {@link CreateAccountInteractorException} is thrown in onError
   * @see CreateAccountInteractorException
   */
  Single<String> createAccount(String name, String mobile, String password);

  /**
   * Throwable class which will be thrown in onError of {@link Subscriber} if createAccount failed,
   * along
   * with the failure reason as a plain
   * text
   */
  class CreateAccountInteractorException extends Throwable {

    public CreateAccountInteractorException(String message) {
      super(message);
    }
  }
}
