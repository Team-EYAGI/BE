package com.example.eyagi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.eyagi.security.JwtProperties;
import com.example.eyagi.security.UserDetailsImpl;


import java.util.Date;

public final class JwtTokenUtils extends JwtProperties {
    /*
    todo: 여기서 리프레시 토큰 생성! 액세스 토큰 : 15분, 리프레시 : 30분. 이정도면 되지 않을까 ?
        -> 리프레시는 현업에서는 비용 때문에 1주일 혹은 2주일 더 길게 가진다고 보았는데, 그건 아마도 로그인 유지 혹은 자동로그인 같은 경우에
        그렇게 사용이 되는 것 같았다.
        우리 같은 경우는 자동로그인이 없고 , 비용문제를 따지기에는 작은 서비스이고 테스트를 해봐야하기 때문에 길게 부여할 수가 없을 듯 하다.
        리프레시 토큰이 만료가 되면, 로그인을 다시 하게 할건데 사용자가 직접 로그인을 다시 해야하는 불편을 없애고 내부에서 다시 해주는건 어떨까?
        보안상 다시 로그인을 시키는것이 좋은 것일까 ?
        이문제에 대해서는 우선 로그인을 하도록 구현을 다 해놓고 조언을 구해봐도 좋을 것 같다.
     */

    private static final int SEC = 1;
    private static final int MINUTE = 60 * SEC;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    // JWT 토큰의 유효기간: 1시간 (단위: seconds)
    private static final int JWT_TOKEN_VALID_SEC = DAY * 3;
    // JWT 토큰의 유효기간: 1시간 (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = JWT_TOKEN_VALID_SEC * 1000;

    public static final String CLAIM_EXPIRED_DATE = "EXPIRED_DATE";
    public static final String CLAIM_USER_NAME = "USER_EMAIL";
    public static final String CLAIM_USER_NIK = "USER_NIK";
    public static final String CLAIM_USER_ROLE = "USER_ROLE";


    public static final String JWT_SECRET = key;  //todo : 시크릿키 보이지않도록 프로퍼티스에 넣어놓기

    public static String generateJwtToken(UserDetailsImpl userDetails) {
        String token = null;
        try {
            token = JWT.create()
                    .withIssuer("sparta")
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())
                    .withClaim(CLAIM_USER_NIK, userDetails.getUserNikName())
                    .withClaim(CLAIM_USER_ROLE, userDetails.getUserRole())

                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간)
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .sign(generateAlgorithm());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

    /*
          todo : refresh token 생성! 사용자 이메일 + 유효기간만 담아주기.
     */
    public static String generateJwtReFreshToken(UserDetailsImpl userDetails) {
        String reFreshToken = null;
        try {

            reFreshToken = JWT.create()
                    .withIssuer("sparta")
                    .withClaim(CLAIM_USER_NAME, userDetails.getUsername())

                    // 토큰 만료 일시 = 현재 시간 + 토큰 유효기간) //30분 부여 .
                    .withClaim(CLAIM_EXPIRED_DATE, new Date(System.currentTimeMillis() + MINUTE * 30 * 1000))
                    .sign(generateAlgorithm());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return reFreshToken;
    }

    /*
    todo : access token을 재발급 해주는 메소드를 생성!
        generateJwtToken 처럼 정적 메소드로 만들어주면, 사용처에서 메소드를 임포트만 해주면 간단히 사용할 수 있다.
        -> 정적 메소드로 만들어주자 . => static method
        한번 사용되었던 access token은 블랙리스트 라고 불리우며 저장 공간을 마련해서 저장을 한다고 한다.
        -> 아마도 이미 만들어진 토큰과 똑같이 생성이 안되는 듯하다. => 블랙리스트에 보관된 토큰이 사용이 될경우, 그건 해커일테니 저장을 하는 것 같다.
     */

    private static Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(JWT_SECRET);
    }
}
