package com.prathab.android.shopping.presentation.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.prathab.android.shopping.R;
import com.prathab.android.shopping.domain.model.Users;
import com.prathab.android.shopping.network.AccountsClient;
import com.prathab.android.shopping.network.services.AccountsService;
import com.prathab.android.shopping.utility.Validators;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {
  @BindView(R.id.editTextEmail) EditText editTextEmail;
  Disposable forgotPasswordDisposable = null;
  ProgressDialog progressDialog = null;
  AlertDialog.Builder builder = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.buttonResetPassword) public void resetPassword() {
    String inputEmail = editTextEmail.getText().toString().trim();
    if (!Validators.isMobileValid(inputEmail)) {
      Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show();
      return;
    }
    Users usersRequest = new Users.Builder().setEmail(inputEmail).build();

    AccountsService accountsService = AccountsClient.getClient().create(AccountsService.class);

    Observable<Response<Users>> responseObservable = accountsService.login(usersRequest);

    progressDialog.setTitle("Please wait");
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Resetting Password");
    progressDialog.show();

    builder.setPositiveButton("Okay", null).setTitle("Error");

    forgotPasswordDisposable = responseObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(usersResponse -> {
          progressDialog.dismiss();
          switch (usersResponse.code()) {
            case 200:
              forgotPasswordSuccessful(usersResponse);
              break;
            case 401:
              builder.setMessage("You did not update email address");
              break;
            case 404:
              builder.setMessage("Please try again later");
              break;
            default:
              builder.setMessage("Unexpected Error");
              break;
          }
          if (usersResponse.code() != 200) {
            builder.create().show();
          }
        }, throwable -> {
          progressDialog.dismiss();
          builder.setMessage(throwable.getLocalizedMessage());
          builder.create().show();
        });
  }

  @Override protected void onStop() {
    super.onStop();
    if (forgotPasswordDisposable != null) forgotPasswordDisposable.dispose();
  }

  private void forgotPasswordSuccessful(Response<Users> usersResponse) {
    //Write JWT to shared prefs
    progressDialog.dismiss();

    new AlertDialog.Builder(this).setTitle("Successful")
        .setMessage("Check your email for instructions")
        .setPositiveButton("Okay", (dialog, which) -> finish())
        .create()
        .show();
  }
}
