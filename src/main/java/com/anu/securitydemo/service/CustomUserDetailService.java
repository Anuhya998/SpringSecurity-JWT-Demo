package com.anu.securitydemo.service;

import com.anu.securitydemo.entity.CustomUserDetails;
import com.anu.securitydemo.entity.User;
import com.anu.securitydemo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
public class CustomUserDetailService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("User Not found");
        }
        return new CustomUserDetails(user);
    }
}
