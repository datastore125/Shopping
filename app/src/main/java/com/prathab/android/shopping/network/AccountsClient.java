package com.prathab.android.shopping.network;

import com.prathab.android.shopping.constants.EndPoints;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Class which returns a Retrofit client
 */
public class AccountsClient {
  private static Retrofit retrofit = null;

  public static Retrofit getClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder().baseUrl(EndPoints.ACCOUNTS)
          .addConverterFactory(MoshiConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .build();
    }
    return retrofit;
  }
}
