package com.example.eyagi.Interceptor;



import com.example.eyagi.model.UserRole;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) //실행시 동작
public @interface Auth {
    UserRole authority() default UserRole.SELLER;
}
