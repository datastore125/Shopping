package com.prathab.android.shopping.domain.repository;

import io.reactivex.Single;

/**
 * Repository interface to handle Login
 */
public interface LoginRepository {

  /**
   * @param loginRepositoryRequest {@link LoginRepositoryRequest} which contains the mobile and
   * password for login
   * @return {@link Single} {@link LoginRepositoryResponse} is returned in onSuccess and {@link
   * LoginRepositoryException} is thrown in onError
   * @see LoginRepositoryException
   */
  Single<LoginRepositoryResponse> login(LoginRepositoryRequest loginRepositoryRequest);

  /**
   * Request class which {@link #login(LoginRepositoryRequest)} method gets as input
   */
  class LoginRepositoryRequest {
    String mobile;
    String password;

    public LoginRepositoryRequest(String mobile, String password) {
      this.mobile = mobile;
      this.password = password;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password.trim();
    }

    public String getMobile() {
      return mobile;
    }
  }

  /**
   * Response class which {@link #login(LoginRepositoryRequest)} method returns in onSuccess
   */
  class LoginRepositoryResponse {
    private String jwtToken;

    public LoginRepositoryResponse(String jwtToken) {
      this.jwtToken = jwtToken;
    }
  }

  /**
   * Exception which will be thrown in onError when {@link #login(LoginRepositoryRequest)} Fails
   * Message contains HTTP response code as a String returned by REST Api
   */
  class LoginRepositoryException extends Throwable {
    public LoginRepositoryException(String message) {
      super(message);
    }
  }
}
