package com.example.eyagi.service;

import com.example.eyagi.security.jwt.HeaderTokenExtractor;
import com.example.eyagi.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final HeaderTokenExtractor headerTokenExtractor;
    private final JwtDecoder jwtDecoder;
    private final RedisService redisService;


    public String refreshCheck (HttpServletRequest request){
        String tokenPayload = request.getHeader("RefreshToken");
        if (tokenPayload == null) {
            System.out.println("올바른 토큰 정보가 아닙니다.");
            throw new NullPointerException("Refresh is Null");
        }
        return headerTokenExtractor.extract(tokenPayload, request);
    }

    public void reRefresh (String email, HttpServletRequest request) {
        String refreshToken = refreshCheck(request);
        String userEmail = jwtDecoder.decodeRefresh(refreshToken);

        //레디스에 username 과 같은 키 찾아서 토큰 비교 후, 액세스 재발급 진행
        String originTokenName = redisService.getValues(refreshToken);

        if (originTokenName == null) {
            throw new NullPointerException("re-login");  //재로그인해야함.
        } else {
            if (!originTokenName.equals(userEmail)) {
                throw new IllegalArgumentException("리프레시 토큰 정보가 일치 하지 않습니다.");
            }
        }
    }


}
