//package com.example.eyagi.security;
//
//import com.example.eyagi.service.RedisService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@RequiredArgsConstructor
//public class CustomLogoutHandler implements LogoutHandler {
//
//    @Resource(name="redisService")
//    private final RedisService redisService;
//
////    private final RedisService redisService;
//
//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        //todo : refresh token을 레디스에서 삭제하는 로직 추가.
//        //회원이 로그아웃 없이 브라우져를 종료하면 어떻게 되는걸까 ?
//            redisService.delValues(request.getHeader("RefreshToken"));
//        //로그아웃 api 쏠때 해더에 리프레시 토큰 담아달라고 하기!
//
//    }
//}
