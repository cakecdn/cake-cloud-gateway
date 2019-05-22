package net.cakecdn.cloud.gateway.all.service;

import net.cakecdn.cloud.gateway.all.domain.User;
import net.cakecdn.cloud.gateway.all.domain.dto.JwtUser;
import net.cakecdn.cloud.gateway.all.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service("jwtUserDetails")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username Not Found, Please Check."));
        User user = userOptional.get();

        return new JwtUser(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
