
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
    private final HeaderTokenExtractor headerTokenExtractor;
    private final UserRepository userRepository;


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
        // 어노테이션 없는 경우
        if (Auth == null) {
            return true;
        }
//헤더에서 토큰을 디코딩하고 멤버에 해당하는 권한을 가지고 있는 지 검사. admin인지 체크 해야함.

        final String header = request.getHeader("Authorization");
        log.debug("header가 뭘까"+header);

        final String HEADER_PREFIX = "Bearer ";

        String role =jwtDecoder.decodeUserRole( header.substring(
                HEADER_PREFIX.length(),
                header.length()
        ));

        if(role.equals("ROLE_USER")){
            throw new IllegalAccessException("권한이 없습니다."); //500에러가 뜸. 400번대 에러로 변경할 수 없을까 ?
        }
        if(Auth.authority()==UserRole.ADMIN){
            if(!role.equals("ROLE_ADMIN")){
                throw new IllegalAccessException("관리자 권한이 없습니다.");
            }
        }
        return true;
    }
}



