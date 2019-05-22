package net.cakecdn.cloud.gateway.register.service;

import net.cakecdn.cloud.gateway.all.domain.Role;
import net.cakecdn.cloud.gateway.all.domain.User;
import net.cakecdn.cloud.gateway.all.repository.RoleRepository;
import net.cakecdn.cloud.gateway.all.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public RegisterServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void doRegister(RegisterForm form) {
        List<Role> roles = new LinkedList<>();
        roles.add(roleRepository.findByName("ROLE_TENANT")); // 默认注册的用户为TENAN(租客)用户组
        User user = new User(form, roles);
        userRepository.save(user);
    }
}
