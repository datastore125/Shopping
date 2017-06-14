package com.prathab.android.shopping.domain.repository;

import io.reactivex.Single;

/**
 * Repository interface to handle createAccount
 */
public interface CreateAccountRepository {

  /**
   * @param createAccountRepositoryRequest {@link CreateAccountRepositoryRequest} which contains
   * the
   * name,
   * mobile and
   * password for createAccount
   * @return {@link Single} {@link CreateAccountRepositoryResponse} is returned in onSuccess and
   * {@link
   * CreateAccountRepositoryException} is thrown in onError
   * @see CreateAccountRepositoryException
   */
  Single<CreateAccountRepositoryResponse> createAccount(
      CreateAccountRepositoryRequest createAccountRepositoryRequest);

  /**
   * Request class which {@link #createAccount(CreateAccountRepositoryRequest)} method gets as input
   */
  class CreateAccountRepositoryRequest {
    String name;
    String mobile;
    String password;

    public CreateAccountRepositoryRequest(String name, String mobile, String password) {
      this.name = name;
      this.mobile = mobile;
      this.password = password;
    }

    public String getName() {
      return name;
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
   * Response class which {@link #createAccount(CreateAccountRepositoryRequest)} method returns in
   * onSuccess
   */
  class CreateAccountRepositoryResponse {
    private String jwtToken;

    public CreateAccountRepositoryResponse(String jwtToken) {
      this.jwtToken = jwtToken;
    }
  }

  /**
   * Exception which will be thrown in onError when {@link #createAccount(CreateAccountRepositoryRequest)}
   * Fails
   * Message contains HTTP response code as a String returned by REST Api
   */
  class CreateAccountRepositoryException extends Throwable {
    public CreateAccountRepositoryException(String message) {
      super(message);
    }
  }
}
