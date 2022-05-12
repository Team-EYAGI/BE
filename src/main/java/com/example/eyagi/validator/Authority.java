package com.example.eyagi.validator;


import com.example.eyagi.model.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) //이건 뭘까?
public @interface Authority {

    UserRole target();
}
