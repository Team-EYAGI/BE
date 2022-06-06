package com.example.eyagi.validator;


import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserValidator {
    public static void checkUser(Optional<User> found, SignupRequestDto signupRequestDto) {
        if(found.isPresent()){
            throw new IllegalArgumentException("아이디가 중복됩니다.");
        }
        if(!signupRequestDto.getPasswordCheck().equals(signupRequestDto.getPassword())){
            throw new NullPointerException("비밀번호 확인이 다릅니다.");
        }
    }
}
