package com.prathab.android.shopping.presentation.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.prathab.android.shopping.domain.repository.LoginRepository;
import com.prathab.android.shopping.presentation.presenters.LoginPresenter;
import com.prathab.android.shopping.presentation.presenters.impl.LoginPresenterImpl;
import javax.inject.Inject;

/**
 * Login Activity which handles user Login
 */
public class Login extends AppCompatActivity implements LoginPresenter.View {
  @BindView(R.id.editTextMobile) EditText editTextMobile;
  @BindView(R.id.editTextPassword) EditText editTextPassword;
  @BindView(R.id.buttonLogin) Button buttonLogin;
  ProgressDialog progressDialog = null;
  AlertDialog.Builder builder = null;
  @Inject LoginRepository mLoginRepository;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    ((ShoppingApplication) getApplication()).getAppComponent().inject(this);

    builder = new AlertDialog.Builder(this);
    builder.setPositiveButton("Okay", null).setTitle("Error");

    progressDialog = new ProgressDialog(this);
  }

  @OnClick(R.id.buttonLogin) public void login() {
    final String inputMobileNumber = editTextMobile.getText().toString();
    final String inputPassword = editTextPassword.getText().toString();

    LoginPresenter loginPresenter = new LoginPresenterImpl(this, mLoginRepository);
    loginPresenter.login(inputMobileNumber, inputPassword);
  }

  @Override protected void onStop() {
    super.onStop();
    //TODO Stop Retrofit Call here
  }

  @OnClick(R.id.buttonSignUp) public void createAccount() {
    startActivity(new Intent(Login.this, CreateAccount.class));
    finish();
  }

  @Override public void displayLoginSuccess(String message) {
    //TODO Start MainActivity
    Toast.makeText(this, "Login Successful!!", Toast.LENGTH_SHORT).show();
    //TODO call finish();
  }

  @Override public void displayLoginFailure(String error) {
    builder.setMessage(error).create().show();
  }

  @Override public void showProgress() {
    progressDialog.setTitle("Please wait");
    progressDialog.setMessage("Logging In");
    progressDialog.setIndeterminate(true);
    progressDialog.show();
  }

  @Override public void hideProgress() {
    progressDialog.dismiss();
  }

  @Override public void lockInput() {
    buttonLogin.setEnabled(false);
  }

  @Override public void unlockInput() {
    buttonLogin.setEnabled(true);
  }
}