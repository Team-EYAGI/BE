package com.example.eyagi.validator;


import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthorityInterceptor implements HandlerInterceptor {

}

/*

    관리자안에는 셀러, 회원의 권한이 포함되어있고
    셀러안에는 회원의 권한도 포함되어 있고
    회원은 회원 권한만

    회원  < 셀러 < 관리자
    회원 -> 로그인을 했냐 안했냐  -> 셀러 가능 , 관리자 가능
    셀러 -> 회원 안됨. 관리자 가능
    관리자 -> 관리자만


 */