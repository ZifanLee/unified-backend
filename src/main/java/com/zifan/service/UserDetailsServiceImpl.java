package com.zifan.service;

import com.zifan.exception.bussiness.DuplicateUserException;
import com.zifan.model.User;
import com.zifan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @SuppressWarnings("unused")
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findByUsername(username);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("Can not find User with userName: "+username);
        }
        if (users.size() == 1) {
            return users.get(0);
        } else {
            throw new DuplicateUserException("Find more than one user with userName: "+username);
        }
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Can not find User Email: "+email);
        }
    }
}
