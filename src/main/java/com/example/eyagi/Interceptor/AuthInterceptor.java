
package com.example.eyagi.Interceptor;

import com.example.eyagi.model.UserRole;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.security.jwt.HeaderTokenExtractor;
import com.example.eyagi.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private  final JwtDecoder jwtDecoder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Auth Auth = null;

        // 현재 입력으로 들어온 메소드가 어노테이션이 붙어있는지 확인 후 그렇지 않으면 그냥 넘어감.
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //어노테이션이 포함되어 있는지 검사
        HandlerMethod hm = (HandlerMethod) handler;
        Auth = ((HandlerMethod) handler).getMethodAnnotation(Auth.class);
        // 제작한 어노테이션 없는 경우
        if (Auth == null) {
            return true;
        }
        //헤더에서 토큰을 꺼내옴.
        final String header = request.getHeader("Authorization");

        final String HEADER_PREFIX = "Bearer ";

        String role =jwtDecoder.decodeUserRole( header.substring(
                HEADER_PREFIX.length(),
                header.length()
        ));
        /*
        비회원의 경우, 시큐리티에서 토큰 유무를 따기지 때문에 굳이 만들 필요 없음.
         user이면 예외 발생. => seller 혹은 admin만 가능.
        제작한 어노테이션에 admin이라고 적혀있다면, 접속을 시도한 유저의 role도 admin 이여야함.
         user와 admin권한 조건문으로 제한을 둠으로써 seller에 대한 처리가 자동으로 되었음.
         */
        if(role.equals("ROLE_USER")){
            throw new IllegalAccessException("권한이 없습니다.");
        }
        if(Auth.authority()==UserRole.ADMIN){
            if(!role.equals("ROLE_ADMIN")){
                throw new IllegalAccessException("관리자 권한이 없습니다.");
            }
        }
        return true;
    }
}



