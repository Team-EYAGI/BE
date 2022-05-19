package com.example.eyagi.model;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Follow extends AbstractPersistable<Long> {
//pk Long으로 자동생성


    @ManyToOne
    @JoinColumn(name = "followerId")
    private User follower; //팔로우를 신청한 사람 .일반 사용자

    @ManyToOne
    @JoinColumn(name = "followedId")
    private User followed; //팔로우를 신청 받은 사람 . 크리에이터

}

