package com.prathab.android.shopping.network.services;

import com.prathab.android.shopping.domain.model.Users;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Retrofit Service class for Accounts activity
 */
public interface AccountsService {
  @POST("login") Observable<Response<Users>> login(@Body Users users);

  @POST("create") Observable<Response<Users>> createAccount(@Body Users users);

  @POST("forgot") Observable<Response<Users>> forgotPassword(@Body Users users);
}

