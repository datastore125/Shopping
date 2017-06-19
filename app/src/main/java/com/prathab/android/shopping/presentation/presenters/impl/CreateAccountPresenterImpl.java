package com.prathab.android.shopping.presentation.presenters.impl;

import com.prathab.android.shopping.domain.interactors.CreateAccountInteractor;
import com.prathab.android.shopping.domain.interactors.impl.CreateAccountInteractorImpl;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import com.prathab.android.shopping.presentation.presenters.CreateAccountPresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    createAccountInteractor.execute(name, mobile, password)
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
        .subscribe(s -> mView.displayCreateAccountSuccess(s),
            throwable -> mView.displayCreateAccountFailure(throwable.getMessage()));
  }
}
