package com.prathab.android.shopping.presentation.presenters.impl;

import com.prathab.android.shopping.domain.interactors.LoginInteractor;
import com.prathab.android.shopping.domain.interactors.impl.LoginInteractorImpl;
import com.prathab.android.shopping.domain.repository.LoginRepository;
import com.prathab.android.shopping.presentation.presenters.LoginPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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
    mView.showProgress();

    LoginInteractor loginInteractor = new LoginInteractorImpl(mLoginRepository);
    loginInteractor.login(mobile, password)
        .doFinally(new Action() {
          @Override public void run() throws Exception {
            mView.hideProgress();
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
          @Override public void accept(@NonNull String s) throws Exception {
            mView.displayLoginSuccess(s);
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(@NonNull Throwable throwable) throws Exception {
            mView.displayLoginFailure(throwable.getMessage());
          }
        });
  }
}
