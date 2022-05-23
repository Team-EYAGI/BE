package com.example.eyagi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //todo : refresh token을 레디스에서 삭제하는 로직 추가.
        //회원이 로그아웃 없이 브라우져를 종료하면 어떻게 되는걸까 ?

    }
}
