package com.example.eyagi.validator;


import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class UserValidator {
    public static void checkUser(Optional<User> found, SignupRequestDto signupRequestDto) {
        Matcher match = Pattern.compile("(\\w)\\1\\1").matcher(signupRequestDto.getPassword());
        if(match.find()){
            throw new NullPointerException("비밀번호는 중복된 글자가 3개 미만이어야 합니다.");
        }
        if(found.isPresent()){
            throw new NullPointerException("아이디가 중복됩니다.");
        }
        if(!signupRequestDto.getPasswordCheck().equals(signupRequestDto.getPassword())){
            throw new NullPointerException("비밀번호 확인이 다릅니다.");
        }
    }
}
//    아이디 형식: 최소 6자 이상, 알파벳 소문자(a~z), 숫자(0~9)를 포함 이메일형식
//   =>
//        비번 영문, 숫자, 특수문자(공백제외)만 허용, 2개 이상 조합
//
//        비번체크 비번이랑 같은지만
//
//        닉넴 닉네임(이름) 형식: 한글 또는 알파벳 대소문자(a~z, A~Z)