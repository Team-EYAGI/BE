package com.example.eyagi.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z]{2,8}$")
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String username;

//    @Pattern(regexp = "^(?=.*\\d-[a-zA-Z])(?=.*[a-zA-Z][!@#$%^&*])(?=.*\\d-[!@#$%^&*]){8,25}$") // 이게 이상하게 안먹네용..!
    @NotBlank(message = "패스워드는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "패드워드 재확인은 필수 항목입니다.")
    private String passwordCheck;

//    아이디 형식: 최소 6자 이상, 알파벳 소문자(a~z), 숫자(0~9)를 포함 이메일형식
//   =>
//        비번 영문, 숫자, 특수문자(공백제외)만 허용, 2개 이상 조합  8~25자
//  =>
//        비번체크 비번이랑 같은지만 8~25자
//
//        닉넴 닉네임(이름) 형식: 한글 또는 알파벳 대소문자(a~z, A~Z) 2~8자
}
