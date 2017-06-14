package com.prathab.android.shopping.presentation.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.prathab.android.shopping.R;
import com.prathab.android.shopping.ShoppingApplication;
import com.prathab.android.shopping.domain.repository.CreateAccountRepository;
import com.prathab.android.shopping.presentation.presenters.CreateAccountPresenter;
import com.prathab.android.shopping.presentation.presenters.impl.CreateAccountPresenterImpl;
import javax.inject.Inject;

/**
 * CreateAccount Activity which handles createAccount
 */
public class CreateAccount extends AppCompatActivity implements CreateAccountPresenter.View {
  @BindView(R.id.editTextName) EditText editTextName;
  @BindView(R.id.editTextMobile) EditText editTextMobile;
  @BindView(R.id.editTextPassword) EditText editTextPassword;
  @BindView(R.id.editTextConfirmPassword) EditText editTextConfirmPassword;
  @BindView(R.id.buttonCreateAccount) Button buttonCreateAccount;
  ProgressDialog progressDialog = null;
  AlertDialog.Builder builder = null;
  @Inject CreateAccountRepository mCreateAccountRepository;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);
    ButterKnife.bind(this);
    ((ShoppingApplication) getApplication()).getAppComponent().inject(this);

    builder = new AlertDialog.Builder(this);
    builder.setPositiveButton("Okay", null).setTitle("Error");

    progressDialog = new ProgressDialog(this);
  }

  @OnClick(R.id.buttonCreateAccount) public void createAccount() {
    final String inputName = editTextName.getText().toString();
    final String inputMobileNumber = editTextMobile.getText().toString();
    final String inputPassword = editTextPassword.getText().toString();
    final String inputConfirmPassword = editTextConfirmPassword.getText().toString();

    if (!inputPassword.contentEquals(inputConfirmPassword)) {
      Toast.makeText(this, "Passwords mismatch", Toast.LENGTH_SHORT).show();
      return;
    }

    CreateAccountPresenter createAccountPresenter =
        new CreateAccountPresenterImpl(this, mCreateAccountRepository);
    createAccountPresenter.createAccount(inputName, inputMobileNumber, inputPassword);
  }

  @Override protected void onStop() {
    super.onStop();
    //TODO Stop Retrofit Call here
  }

  @Override public void displayCreateAccountSuccess(String message) {
    //TODO Start MainActivity
    Toast.makeText(this, "Account Created Successfully!!", Toast.LENGTH_SHORT).show();
    //TODO call finish();
  }

  @Override public void displayCreateAccountFailure(String error) {
    builder.setMessage(error).create().show();
  }

  @Override public void showProgress() {
    progressDialog.setTitle("Please wait");
    progressDialog.setMessage("Creating Account");
    progressDialog.setIndeterminate(true);
    progressDialog.show();
  }

  @Override public void hideProgress() {
    progressDialog.dismiss();
  }

  @Override public void lockInput() {
    buttonCreateAccount.setEnabled(false);
  }

  @Override public void unlockInput() {
    buttonCreateAccount.setEnabled(true);
  }
}
