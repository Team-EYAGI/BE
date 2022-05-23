package com.example.eyagi.service;

import com.example.eyagi.model.VisitCount;
import com.example.eyagi.repository.VisitCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final VisitCountRepository visitCountRepository;

    /*쿠키 생성 !
    oneTimeCookie 쿠키 생성
    monthCookie 쿠키 생성
*/

    public void selectOneTimeCookie (HttpServletResponse response, VisitCount toDayCount){
        Cookie oneTimeCookie = new Cookie("oneTimeCookie", "Hello");
        oneTimeCookie.setDomain("eyagibook.shop"); //우리 사이트 도메인 이름 넣기 프론트 도메인이겠지 ..?
        oneTimeCookie.setPath("/");
        response.addCookie(oneTimeCookie); //쿠키에 일정 시간을 지정하지 않으면 브라우져가 종료될 때 쿠키도 함께 사라지므로, 방문 횟수를 세기에 적절하다고 판단.
        toDayCount.addCount();
    }

    public void selectMonthCookie(HttpServletResponse response, VisitCount toDayCount) {
        Cookie monthCookie = new Cookie("monthCookie", "Welcome");
        monthCookie.setMaxAge(60 * 60 * 24 * 30); //초단위
        monthCookie.setDomain("eyagibook.shop"); // 우리 사이트 도메인 이름 넣기. 프론트 도메인이겠지 ..?
        monthCookie.setPath("/");
        response.addCookie(monthCookie);
        toDayCount.addVister();
    }

    public VisitCount newDayNewCount(LocalDate nowDay) {
        List<VisitCount> allCount = visitCountRepository.findAll();
        if (allCount.size() == 0) {
            return new VisitCount();
        }
        VisitCount lastDay = allCount.get(allCount.size() - 1);
        return lastDay.newVisitCounter(nowDay);

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
