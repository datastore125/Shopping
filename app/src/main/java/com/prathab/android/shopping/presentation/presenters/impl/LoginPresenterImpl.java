package com.prathab.android.shopping.presentation.presenters.impl;

import com.prathab.android.shopping.domain.interactors.LoginInteractor;
import com.prathab.android.shopping.domain.interactors.impl.LoginInteractorImpl;
import com.prathab.android.shopping.domain.repository.LoginRepository;
import com.prathab.android.shopping.presentation.presenters.LoginPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Concrete Implementation of {@link LoginPresenter}
 */
public class LoginPresenterImpl implements LoginPresenter {
  private LoginPresenter.View mView;
  private LoginRepository mLoginRepository;

  public LoginPresenterImpl(View mView, LoginRepository mLoginRepository) {
    this.mView = mView;
    this.mLoginRepository = mLoginRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override public void login(String mobile, String password) {

    LoginInteractor loginInteractor = new LoginInteractorImpl(mLoginRepository);
    loginInteractor.execute(mobile, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally(() -> {
          mView.hideProgress();
          mView.unlockInput();
        })
        .doOnSubscribe(disposable -> {
          mView.lockInput();
          mView.showProgress();
        })
        .subscribe(s -> mView.displayLoginSuccess(s),
            throwable -> mView.displayLoginFailure(throwable.getMessage()));
  }
}
