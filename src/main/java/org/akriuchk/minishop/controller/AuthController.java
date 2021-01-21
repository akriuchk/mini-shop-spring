package org.akriuchk.minishop.controller;

import org.akriuchk.minishop.dto.UserLoginDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    @PostMapping("/auth/login")
    public UserLoginDto signup(@RequestBody UserLoginDto signInDto) {
        System.out.println(signInDto);
        return signInDto;
    }

    //https://www.baeldung.com/registration-with-spring-mvc-and-spring-security
    //https://www.baeldung.com/spring-security-login
}
