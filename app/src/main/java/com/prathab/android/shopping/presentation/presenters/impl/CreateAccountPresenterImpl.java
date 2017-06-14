package com.prathab.android.shopping.presentation.presenters.impl;

import com.prathab.android.shopping.domain.interactors.CreateAccountInteractor;
import com.prathab.android.shopping.domain.interactors.impl.CreateAccountInteractorImpl;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import com.prathab.android.shopping.presentation.presenters.CreateAccountPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Concrete Implementation of {@link CreateAccountPresenter}
 */
public class CreateAccountPresenterImpl implements CreateAccountPresenter {
  private View mView;
  private CreateAccountRepository mCreateAccountRepository;

  public CreateAccountPresenterImpl(View mView, CreateAccountRepository mCreateAccountRepository) {
    this.mView = mView;
    this.mCreateAccountRepository = mCreateAccountRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override public void createAccount(String name, String mobile, String password) {

    CreateAccountInteractor createAccountInteractor =
        new CreateAccountInteractorImpl(mCreateAccountRepository);
    createAccountInteractor.createAccount(name, mobile, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally(new Action() {
          @Override public void run() throws Exception {
            mView.hideProgress();
            mView.unlockInput();
          }
        })
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override public void accept(@NonNull Disposable disposable) throws Exception {
            mView.lockInput();
            mView.showProgress();
          }
        })
        .subscribe(new Consumer<String>() {
          @Override public void accept(@NonNull String s) throws Exception {
            mView.displayCreateAccountSuccess(s);
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(@NonNull Throwable throwable) throws Exception {
            mView.displayCreateAccountFailure(throwable.getMessage());
          }
        });
  }
}
