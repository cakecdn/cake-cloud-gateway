package net.cakecdn.cloud.gateway.login.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class LoginForm {
    private String username;
    private String cellphone;
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}
