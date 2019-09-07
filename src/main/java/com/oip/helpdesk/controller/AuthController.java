package com.oip.helpdesk.controller;

import com.oip.helpdesk.domain.entities.User;
import com.oip.helpdesk.repository.UserRepository;
import com.oip.helpdesk.security.CustomUserDetails;
import com.oip.helpdesk.security.CustomUserDetailsService;
import com.oip.helpdesk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    UserService userService;

    @GetMapping("/user/custominfo")
    public User userInfo(@RequestHeader("Authorization") String header ) throws UsernameNotFoundException {
        User user = userService.findByToken(header);
        return user;
    }
}
