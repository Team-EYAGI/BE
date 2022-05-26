package com.example.eyagi.security;


import com.example.eyagi.security.jwt.JwtTokenUtils;
import com.example.eyagi.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {



    public static final String AUTH_HEADER = "Authorization";

    public static final String TOKEN_TYPE = "BEARER";

    public static final String Refresh_HEADER = "RefreshToken"; // TODO : RT



    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        /*
        todo : 리프레시 토큰도 함께보내주기 ! 리프레시토큰을 레디스에 저장하기 !
            -> 레디스에 저장할때 , 키 벨류로 저장하고 ! (레디스가 원래 키벨류 방식임 ... )
            토큰 유효시간과 동일하게 시간을 주어서 자동으로 삭제 되게 하자 !
            -> set(token, email, Duration.ofMinutes(3)); 이렇게 매기면 된다고함. => 예제 코드로 하면 3분 이후 삭제된다.
         */

        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        final String RefreshToken = JwtTokenUtils.generateJwtReFreshToken(userDetails); // TODO : RT

        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        response.addHeader(Refresh_HEADER, TOKEN_TYPE + " " + RefreshToken); // TODO : RT


    }

}
