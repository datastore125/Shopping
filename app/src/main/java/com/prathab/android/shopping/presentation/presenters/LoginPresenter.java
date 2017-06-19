package com.prathab.android.shopping.presentation.presenters;

import android.app.Activity;

/**
 * Presenter interface which will be implemented by a concrete Presenter
 */
public interface LoginPresenter {
  /**
   * @param mobile Mobile number entered by user which will be used for Login
   * @param password Password entered by user which will be used for Login
   */
  void login(String mobile, String password);

  /**
   * View interface which will be implemented by an {@link Activity}
   */
  interface View {
    /**
     * @param message Login success message which will be displayed on UI
     */
    void displayLoginSuccess(String message);

    /**
     * @param error Login Failure message which will be displayed on UI
     */
    void displayLoginFailure(String error);

    /**
     * Method to show progress while logging in
     */
    void showProgress();

    /**
     * Method to hide progress after execute response
     */
    void hideProgress();

    /**
     * Method to lock the button during network call
     */
    void lockInput();

    /**
     * Method to unlock the button during network call
     */
    void unlockInput();
  }
}
