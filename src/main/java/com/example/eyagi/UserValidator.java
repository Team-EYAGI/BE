package com.example.eyagi;


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
