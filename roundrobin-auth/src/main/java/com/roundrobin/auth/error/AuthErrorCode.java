package com.roundrobin.auth.error;

import com.roundrobin.error.ErrorCode;

public class AuthErrorCode extends ErrorCode {
  public static final String INVALID_ROLE_ID = "invalid_role_id";
  public static final String INVALID_ROLE_NAME = "invalid_role_name";
  public static final String INVALID_SECRET = "invalid_secret";
  public static final String ACTION_EXPIRED = "action_expired";
  public static final String INVALID_USER_ID = "invalid_user_id";
  public static final String USER_ALREADY_VERIFIED = "user_already_verified";
  public static final String INVALID_USERNAME = "invalid_username";
  public static final String USER_ALREADY_EXIST = "user_already_exist";
}
