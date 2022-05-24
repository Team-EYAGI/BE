package com.example.eyagi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Entity
@Getter
public class VisitCount extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    //    private LocalDate toDay = LocalDate.now(ZoneId.of("Asia/Seoul")); // 오늘
//    private LocalDate toDay = LocalDate.now(); // 오늘

    private int count = 0; // 총 방문 횟수 카운트 oneDayCookie쿠키를 센다

    private int visiter = 0; // 실 이용자 수  monthCookie룰 센다


    public VisitCount newVisitCounter(String nowDate1) {
//        System.out.println("저장 날짜 : " + this.getCreatedAt() + "지금 날짜 : " + nowDate1);
        String [] today = this.getCreatedAt().toString().split("T");
//        System.out.println(today[0]);
//        System.out.println(nowDate1);
        if (!today[0].equals(nowDate1)) {
            return new VisitCount();
        } else {
            return this;
        }
    }

    //카운트 ++ 하기
    public void addCount() {
        this.count++;
    }

    public void addVister() {
        this.visiter++;
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

쿠키를 생성해서 구분해보자 !

1. 쿠키의 유효기간을 설정하지 않기 ! -> 그러면 브라우저가 종료될시에 쿠키도 사라짐. => oneDayCookie
2. 쿠키의 유효기간을 한달 정도로 설정! ->우리가 서비스를 한달정도 ? 할 것 같으니 한달 정도면 충분할 듯 하다. monthCookie

동시접속자 수도 하고 싶다 !!!!
 */
