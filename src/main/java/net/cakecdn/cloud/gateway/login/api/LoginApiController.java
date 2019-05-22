package net.cakecdn.cloud.gateway.login.api;

import net.cakecdn.cloud.gateway.all.domain.dto.AjaxResult;
import net.cakecdn.cloud.gateway.login.domain.LoginForm;
import net.cakecdn.cloud.gateway.login.exception.LoginRejectException;
import net.cakecdn.cloud.gateway.login.exception.UserDisabledRejectException;
import net.cakecdn.cloud.gateway.login.exception.UserNotFoundWhenLoginException;
import net.cakecdn.cloud.gateway.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/public/login")
public class LoginApiController {

    private final LoginService loginService;

    @Autowired
    public LoginApiController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public AjaxResult doLogin(@RequestBody LoginForm form) throws UserDisabledRejectException, UserNotFoundWhenLoginException, LoginRejectException {
        return AjaxResult.success(loginService.doLogin(form));
    }
}
