package com.vova.purchaseservice.data.service;

import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.ex.NotAuthorizedException;
import com.vova.purchaseservice.ex.RegisterUserAlreadyExistsException;
import com.vova.purchaseservice.ex.UserNotFoundException;
import com.vova.purchaseservice.security.DbUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static String getLoginFromSecurityContext() {
        SecurityContext context = Optional.ofNullable(SecurityContextHolder.getContext())
                .orElseThrow(NotAuthorizedException::new);
        Authentication authentication = Optional.ofNullable(context.getAuthentication())
                .orElseThrow(NotAuthorizedException::new);
        DbUserPrincipal authorization = (DbUserPrincipal) Optional.ofNullable(authentication.getPrincipal())
                .orElseThrow(NotAuthorizedException::new);
        return authorization.getUsername();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new DbUserPrincipal(user);
    }

    public User registerUser(String login, String password, String name) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RegisterUserAlreadyExistsException(login);
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        return userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

    public User editUser(String login, String password, String name) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        return userRepository.save(user);
    }

    public User info(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
    }

}
