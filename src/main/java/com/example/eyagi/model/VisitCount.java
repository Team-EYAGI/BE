package com.example.eyagi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Entity
@Getter
public class VisitCount extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private int count = 0; // 총 방문 횟수 카운트 oneDayCookie쿠키를 센다

    private int visiter = 0; // 실 이용자 수  monthCookie룰 센다



    public VisitCount newVisitCounter(String nowDate1) {
        String [] today = this.getCreatedAt().toString().split("T");
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
