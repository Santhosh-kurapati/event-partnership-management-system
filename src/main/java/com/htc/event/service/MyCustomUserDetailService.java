package com.htc.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.htc.event.entity.User;
import com.htc.event.security.UserDetailsImpl;
import com.htc.event.dao.UserRepository;

@Service
public class MyCustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    
    //Converts a database entity User into a Spring Security-compatible object for authentication.
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}

