package com.example.eyagi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    public static String key;

    @Value("${JWT_SECRET}")
    public void setKey(String value) {
        key = value;
    }

}
