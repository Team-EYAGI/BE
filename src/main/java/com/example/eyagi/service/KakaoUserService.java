package com.example.eyagi.service;


import com.example.eyagi.dto.KakaoUserInfoDto;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserLibrary;
import com.example.eyagi.model.UserProfile;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.repository.*;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.security.jwt.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoUserService {

    @Value("${kakao.client_id}")
    private String kakaoClientId;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserLibraryRepository libraryRepository;
    private final UserProfileRepository profileRepository;

    // 카카오 로그인하기
    public KakaoUserInfoDto kakaoLogin(String code, HttpServletResponse res) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);
        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        //3. 카카오 회원가입
        User kakaoUser = registerUserIfNeed(kakaoUserInfoDto);

        // 4. 강제 로그인 처리
        Authentication authentication =forceLogin(kakaoUser);

        //5. jwt토큰 추가
        kakaoUserAuthorization(authentication, res);

        return kakaoUserInfoDto;
    }


    private String getAccessToken (String code) throws JsonProcessingException {
                // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

                // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", "https://eyagi99.shop/user/kakao/callback");
        body.add("code", code);

                // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                        new HttpEntity<>(body, headers);
                RestTemplate rt = new RestTemplate();
                ResponseEntity<String> response = rt.exchange(
                        "https://kauth.kakao.com/oauth/token",
                        HttpMethod.POST,
                        kakaoTokenRequest,
                        String.class
                );

                // HTTP 응답 (JSON) -> 액세스 토큰 파싱
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                return jsonNode.get("access_token").asText();

            }
    private KakaoUserInfoDto getKakaoUserInfo (String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);

        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerUserIfNeed(KakaoUserInfoDto kakaoUserInfoDto) {

        Long kakaoId = kakaoUserInfoDto.getKakaoId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);
        //가입된 유저 확인
//        Optional<User>userCheck = userRepository.findByEmail(kakaoUserInfoDto.getEmail());
//        if(userCheck.isPresent()){
//            throw new IllegalArgumentException("이미 가입된 유저입니다.");
//        }
        if(kakaoUser == null){
// username: kakao nickname
            String nickname = kakaoUserInfoDto.getUsername();
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String email = kakaoUserInfoDto.getEmail();
            UserRole role = UserRole.USER;

            kakaoUser = new User(email, nickname, password, role, kakaoId);
            userRepository.save(kakaoUser);
            UserLibrary userLibrary = new UserLibrary(kakaoUser);
            UserProfile userProfile = new UserProfile(kakaoUser);
            libraryRepository.save(userLibrary);
            profileRepository.save(userProfile);
        }
        return kakaoUser;
    }
    private Authentication forceLogin(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
    private void kakaoUserAuthorization(Authentication authentication, HttpServletResponse res ){
        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
        String token = JwtTokenUtils.generateJwtToken(userDetailsImpl);
        log.info("JWT토큰 : " + token);
        res.addHeader("Authorization", "BEARER" + " " + token);
    }

}