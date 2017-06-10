package com.prathab.android.shopping.presentation.ui.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.prathab.android.shopping.R;
import com.prathab.android.shopping.constants.HttpConstants;
import com.prathab.android.shopping.domain.model.Users;
import com.prathab.android.shopping.network.AccountsClient;
import com.prathab.android.shopping.network.services.AccountsService;
import com.prathab.android.shopping.utility.Validators;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.prathab.android.shopping.constants.SharedPreferencesConstants.SHARED_PREFERENCES_NAME;

public class CreateAccount extends AppCompatActivity {

  @BindView(R.id.editTextName) EditText editTextName;
  @BindView(R.id.editTextMobile) EditText editTextMobile;
  @BindView(R.id.editTextPassword) EditText editTextPassword;

  SharedPreferences sharedPreferences = null;
  Disposable createAccountDisposable = null;
  ProgressDialog progressDialog = null;
  AlertDialog.Builder builder = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);
    ButterKnife.bind(this);
    builder = new AlertDialog.Builder(this);
    progressDialog = new ProgressDialog(this);
  }

  @OnClick(R.id.buttonCreateAccount) public void createAccount() {
    final String inputName = editTextName.getText().toString().trim();
    final String inputMobileNumber = editTextMobile.getText().toString().trim();
    final String inputPassword = editTextPassword.getText().toString().trim();
    if (inputName.isEmpty()
        || !Validators.isMobileValid(inputMobileNumber)
        || inputPassword.isEmpty()) {
      Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show();
      return;
    }

    Users usersRequest = new Users.Builder().setName(inputName)
        .setMobile(inputMobileNumber)
        .setPassword(inputPassword)
        .build();

    AccountsService accountsService = AccountsClient.getClient().create(AccountsService.class);

    Observable<Response<Users>> responseObservable = accountsService.createAccount(usersRequest);

    progressDialog.setTitle("Please wait");
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage("Creating Account");
    progressDialog.show();

    builder.setPositiveButton("Okay", null).setTitle("Error");

    createAccountDisposable = responseObservable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Response<Users>>() {
          @Override public void accept(@NonNull Response<Users> usersResponse) throws Exception {
            progressDialog.dismiss();
            switch (usersResponse.code()) {
              case 200:
                createAccountSuccessful(usersResponse);
                break;
              case 400:
                builder.setMessage("Check your details");
                break;
              case 409:
                builder.setMessage("Account Already Exists");
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
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(@NonNull Throwable throwable) throws Exception {
            progressDialog.dismiss();
            builder.setMessage(throwable.getLocalizedMessage());
            builder.create().show();
          }
        });
  }

  @Override protected void onStop() {
    super.onStop();
    if (createAccountDisposable != null) createAccountDisposable.dispose();
  }

  private void createAccountSuccessful(Response<Users> usersResponse) {
    //Write JWT to shared prefs
    String authToken = usersResponse.headers().get(HttpConstants.HTTP_HEADER_JWT_TOKEN);
    sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

    Log.d("prathab", authToken);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(HttpConstants.HTTP_HEADER_JWT_TOKEN, authToken);

    editor.apply();
    progressDialog.dismiss();

    Toast.makeText(this, "Start Main Activity", Toast.LENGTH_SHORT).show();

    //finish();
  }
}
