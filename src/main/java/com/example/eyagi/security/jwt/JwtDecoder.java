package com.example.eyagi.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtDecoder {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    public String decodeUsername(String token) {
//
//            DecodedJWT decodedJWT = isValidToken(token)
//                    .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다."));
//
//            Date expiredDate = decodedJWT
//                    .getClaim(JwtTokenUtils.CLAIM_EXPIRED_DATE)
//                    .asDate();
//
//            Date now = new Date();
//            if (expiredDate.before(now)) {
//
//            throw new IllegalArgumentException("만료된 토큰입니다.");
////                request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
//                //todo : 유효기간이 끝났을경우 , 재발급을 해야된다는 의미의 에러를 내려주자
//            }
//            String username = decodedJWT
//                    .getClaim(JwtTokenUtils.CLAIM_USER_NAME)
//                    .asString();
//            return username;
//
//    }

    // TODO : RT
    public String decodeUsername(String token, HttpServletResponse response) {

        try {
            DecodedJWT decodedJWT = isValidToken(token)
                    .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다."));

            Date expiredDate = decodedJWT
                    .getClaim(JwtTokenUtils.CLAIM_EXPIRED_DATE)
                    .asDate();

            Date now = new Date();
            if (expiredDate.before(now)) {

                throw new IllegalArgumentException("만료된 토큰입니다.");
                //todo : 유효기간이 끝났을경우 , 재발급을 해야된다는 의미의 에러를 내려주자
            }
            String username = decodedJWT
                    .getClaim(JwtTokenUtils.CLAIM_USER_NAME)
                    .asString();
            return username;
        }catch (Exception e){
            response.addHeader("Authorization", "TimeOut");
            return e.getMessage();
        }

    }

    public String decodeUserRole(String token) {
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다."));

        Date expiredDate = decodedJWT
                .getClaim(JwtTokenUtils.CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if (expiredDate.before(now)) {
            throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
        }

        String userRole = decodedJWT
                .getClaim(JwtTokenUtils.CLAIM_USER_ROLE)
                .asString();


        return  userRole;
    }


    public String decodeRefresh(String token) {

      DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다."));

        Date expiredDate = decodedJWT
                .getClaim(JwtTokenUtils.CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
           if (expiredDate.before(now)) {

               throw new IllegalArgumentException("TimeOut");
//                request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
               //todo : 유효기간이 끝났을경우 , 재발급을 해야된다는 의미의 에러를 내려주자
           }
           String username = decodedJWT
                   .getClaim(JwtTokenUtils.CLAIM_USER_NAME)
                   .asString();
           return username;
//        return username;

    }

    private Optional<DecodedJWT> isValidToken(String token) {
        DecodedJWT jwt = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtils.JWT_SECRET);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();

            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Optional.ofNullable(jwt);
    }

}
