package com.prathab.android.shopping.presentation.presenters;

import android.app.Activity;

/**
 * Presenter interface which will be implemented by a concrete Presenter
 */
public interface CreateAccountPresenter {
  /**
   * @param name Name entered by the user
   * @param mobile Mobile number entered by user which will be used for execute
   * @param password Password entered by user which will be used for execute
   */
  void createAccount(String name, String mobile, String password);

  /**
   * View interface which will be implemented by an {@link Activity}
   */
  interface View {
    /**
     * @param message execute success message which will be displayed on UI
     */
    void displayCreateAccountSuccess(String message);

    /**
     * @param error execute Failure message which will be displayed on UI
     */
    void displayCreateAccountFailure(String error);

    /**
     * Method to show progress while account creation
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
