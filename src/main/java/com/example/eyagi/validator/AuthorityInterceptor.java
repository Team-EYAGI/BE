package com.example.eyagi.validator;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthorityInterceptor implements HandlerInterceptor {

    private final Authentication authentication;

    public AuthorityInterceptor(Authentication authentication) {
        this.authentication = authentication;
    }
}
