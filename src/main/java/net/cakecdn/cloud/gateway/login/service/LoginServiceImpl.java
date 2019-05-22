package net.cakecdn.cloud.gateway.login.service;

import net.cakecdn.cloud.gateway.all.domain.User;
import net.cakecdn.cloud.gateway.all.repository.UserRepository;
import net.cakecdn.cloud.gateway.all.util.JwtTokenUtil;
import net.cakecdn.cloud.gateway.login.exception.LoginRejectException;
import net.cakecdn.cloud.gateway.login.exception.UserDisabledRejectException;
import net.cakecdn.cloud.gateway.login.exception.UserNotFoundWhenLoginException;
import net.cakecdn.cloud.gateway.login.domain.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Value("${cake.authorization-token-prefix:Bearer }")
    private String authorizationTokenPrefix;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public LoginServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            @Qualifier("jwtUserDetails") UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public String doLogin(LoginForm form) throws UserNotFoundWhenLoginException, LoginRejectException, UserDisabledRejectException {
        User user = userRepository.findByUsername(form.getUsername()).orElseThrow(UserNotFoundWhenLoginException::new);

        if (user.isDisabled()) {
            throw new UserDisabledRejectException();
        }

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user.getUsername(), form.getPassword());
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        return authorizationTokenPrefix + jwtTokenUtil.generateToken(user, userDetails);
    }
}
