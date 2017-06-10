package com.prathab.android.shopping.domain.interactors;

import io.reactivex.Single;
import org.reactivestreams.Subscriber;

public interface LoginInteractor {

  /**
   * @param mobile Mobile number entered by the user
   * @param password Password entered by the user
   * @return {@link Single} String message which should be displayed in UI will be returned in
   * onSuccess and {@link LoginInteractorException} is thrown in onError
   * @see LoginInteractorException
   */
  Single<String> login(String mobile, String password);

  /**
   * Throwable class which will be thrown in onError of {@link Subscriber} if login failed, along
   * with the failure reason as a plain
   * text
   */
  class LoginInteractorException extends Throwable {

    public LoginInteractorException(String message) {
      super(message);
    }
  }
}
