package com.anu.securitydemo.service;

import com.anu.securitydemo.entity.User;
import com.anu.securitydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String verify(User user){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//        User user1 = userRepository.findByUserName(user.getUserName());
        if (authentication.isAuthenticated())
            return jwtService.generateToken(user);
//            return "45678909874563459837405305677r3546563!";
        return "Failure ";

        /*User user1 = userRepository.findByUserName(user.getUserName());
        log.info("user:" + user.getUserName());
        log.info("db pass:" + user1.getPassword());
        log.info("entered pass:" + user.getPassword());
        if (user1 != null) {
            return (user1.getPassword().equals(user.getPassword()) && user1.getUserName().equals(user.getUserName())) ? ResponseEntity.ok("success") : ResponseEntity.ok("failure");
        } else {
            return ResponseEntity.badRequest().body("User not found");
//            throw new UsernameNotFoundException("User not found");
        }*/
    }
}
