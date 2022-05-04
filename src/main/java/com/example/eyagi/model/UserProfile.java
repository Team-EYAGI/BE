package com.example.eyagi.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class UserProfile extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String userImage;  //S3 경로

    private String originImage;  //S3 파일 이름.

    private String introduce;  //자기소개글.

    @OneToOne(mappedBy = "userProfile")
    private AudioFile userVoice;

    @OneToOne
    @JoinColumn(name = "USER_ID") //유저테이블과 프로필 테이블중 프로필 테이블에 작업수행이 더 빈번할 것으로 판단되어 프로필에 외래키를 관리하도록 줌.
    private User user;


}
