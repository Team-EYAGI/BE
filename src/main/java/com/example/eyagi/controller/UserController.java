package com.example.eyagi.controller;

import com.example.eyagi.dto.KakaoUserInfoDto;
import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.dto.UserDto;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.security.jwt.JwtDecoder;
import com.example.eyagi.service.KakaoUserService;
import com.example.eyagi.service.RedisService;
import com.example.eyagi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.example.eyagi.security.FormLoginSuccessHandler.Refresh_HEADER;
import static com.example.eyagi.security.jwt.JwtTokenUtils.generateJwtReFreshToken;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;
    private final JwtDecoder jwtDecoder;
    private final RedisService redisService;
    //회원가입
    @PostMapping("/user/join")
    public ResponseEntity<String> registerUser(@Valid  @RequestBody SignupRequestDto signupRequestDto){
        return userService.registerUser(signupRequestDto);
    }

    //이메일 중복확인
    @PostMapping("/user/email/check")
    public String userEmailCheck (@RequestBody SignupRequestDto signupRequestDto) {
        return userService.userEmailCheck(signupRequestDto.getEmail());
    }

    //닉네임 중복확인
    @PostMapping("/user/userName/check")
    public String usernameCheck (@RequestBody SignupRequestDto signupRequestDto) {
        return userService.userNameCheck(signupRequestDto.getUsername());
    }


    // 마이페이지
    @PostMapping("/user/mypage")
    public ResponseEntity<UserDto.MypageDto> viewMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.viewMyPage(userDetails);
    }



    //카카오로그인
    @GetMapping("/user/kakao/callback")
    public KakaoUserInfoDto kakaoLogin(@RequestParam String code,HttpServletResponse res) throws JsonProcessingException {
        return kakaoUserService.kakaoLogin(code, res);
    }


    //todo : access token 재 발급 요청.
    @PostMapping("/re/refresh")
    public void reRefreshToken(HttpServletRequest request, HttpServletResponse response,
                               @AuthenticationPrincipal UserDetailsImpl userDetails){
        //프론트에서 리프레시토큰을 받고, 거기 있는 사용자 정보를 꺼내고 레디스에 잇는 거랑 비교해서 일치하는 정보의 토큰과 일치하면,
        //엑세스 토큰을 새로 발급해준다.
        //만약 리프레시 토큰도 만료가 되었다면, 로그인을 다시하게 한다.
        String username = jwtDecoder.decodeRefresh(request.getHeader("RefreshToken"));
        if (!userDetails.getUsername().equals(username)){
            throw new IllegalArgumentException("현재 사용자와 일치하지 않는 리프래쉬 토큰입니다.");
        }
        //레디스에 username 과 같은 키 찾아서 토큰 비교 후, 액세스 재발급 진행
        String originToken = redisService.getValues(username);
        if(originToken == null){
             throw new NullPointerException("re-login");
        } else {
            if (!originToken.equals(request.getHeader("RefreshToken"))){
                throw new IllegalArgumentException("리프레시 토큰 정보가 일치 하지 않습니다.");
            }
            response.addHeader(Refresh_HEADER, generateJwtReFreshToken(userDetails));
        }
    }


    @GetMapping ("/sellerList")
    public ResponseEntity sellerList(Pageable pageable) {
        return ResponseEntity.ok(userService.findSellerList(pageable));
    }

}
