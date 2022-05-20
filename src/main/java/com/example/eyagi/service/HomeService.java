package com.example.eyagi.service;

import com.example.eyagi.model.Timestamped;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class HomeService extends Timestamped {

    /*쿠키 생성 !
    oneTimeCookie 쿠키 생성
    monthCookie 쿠키 생성
*/


    public void selectOneTimeCookie (HttpServletResponse response){

        Cookie oneTimeCookie = new Cookie("oneTimeCookie", null);
//        oneDayCookie.setMaxAge(60*60*6);  //초단위
        response.addCookie(oneTimeCookie);

    }
    public void  selectMonthCookie (HttpServletResponse response){
        Cookie monthCookie = new Cookie("monthCookie", null);
        monthCookie.setMaxAge(60*60*24*30); //초단위
        response.addCookie(monthCookie);
    }

}
/*
하고싶은거
1. 일별 접속자 수 (1인 중복 가능할수 있음 . 새로고침한다던가.. 아침에 들어왔다 밤에 들어왔다던가.. 다 포함.
    일일 시간을 정해야함. 지금 로컬 타임이랑 년훨일. 변수에 있는 로컬 타임이랑 비교해서 넘겨주면 될듯 ?
    사용자가 들어온 시점의 로컬 타임과 , 현재 만들어져서 사용자 수가 더해지고 있는
    방법 - 1.사용자 ip 값을 무조건 다 받는다 ! 중복허용
2. 접속한 개인 수 ( 중복 없이. 순수 사람 수를 셈. 방법- 1. 사용자 ip 주소로 계산. 중복 안됨.
3. 가입한 회원수를 세어서 2번과 비교하여 회원 전환률 계산.
 */
