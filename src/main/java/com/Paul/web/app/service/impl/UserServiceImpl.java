package com.Paul.web.app.service.impl;

import com.Paul.web.app.entity.Role;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.exception.BuisnessException;
import com.Paul.web.app.repository.RoleRepository;
import com.Paul.web.app.repository.UserRepository;
import com.Paul.web.app.security.TokenHandler;
import com.Paul.web.app.security.UserDetails;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TokenHandler tokenHandler;

    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }


    @Override
    public User registerNewUser(User newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        newUser.setUserRoles(new HashSet<Role>(
                Arrays.asList(roleRepository.findByName("USER"))));
        return userRepository.save(newUser);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getCurrentUser(String token) {
        User user = tokenHandler.parseUserFromToken(token).getUser();
        String email = user.getEmail();
        user = userRepository.findByEmail(user.getEmail());
        return user;
    }

    @Override
    public User getUserFromSecurityContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

        userDetails.getUser().getEmail();
        return new User();
    }

    @Override
    public Set<User> findByOrg(String token) {
        return getCurrentUser(token).getOrganisation().getParticipants();
    }

    @Override
    public User findUserById(int userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    @Override
    public User assignRoles(User userChanged, String token) {
        User user = findUserById(userChanged.getId());
        if( !getCurrentUser(token).getOrganisation().getParticipants().contains(user)) {
            throw new BuisnessException("this user is not in your organisation");
        }
        for (Role role : userChanged.getUserRoles()) {
            user.getUserRoles().add(role);
        }
        return saveUser(user);
    }
}
