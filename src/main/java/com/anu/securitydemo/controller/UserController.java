package com.anu.securitydemo.controller;

import com.anu.securitydemo.entity.User;
import com.anu.securitydemo.repository.UserRepository;
import com.anu.securitydemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
//        return userRepository.save(user);
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.verify(user));
    }


}
