package net.cakecdn.cloud.gateway.login.service;

import net.cakecdn.cloud.gateway.login.exception.LoginRejectException;
import net.cakecdn.cloud.gateway.login.exception.UserDisabledRejectException;
import net.cakecdn.cloud.gateway.login.exception.UserNotFoundWhenLoginException;
import net.cakecdn.cloud.gateway.login.domain.LoginForm;

public interface LoginService {
    String doLogin(LoginForm form) throws UserNotFoundWhenLoginException, LoginRejectException, UserDisabledRejectException;
}
