package com.example.eyagi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class UserProfile extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String userImage;  //S3 경로

    private String originImage;  //S3 파일 이름.

    private String introduce;  //자기소개글. - 판매자만

    @OneToOne
    @JoinColumn(name = "AUDIOFILE_ID")
    private AudioFile userVoice; //내 보이스 - 판매자만 파일 크기 지정해줘야함

    @OneToOne
    @JoinColumn(name = "USER_ID") //유저테이블과 프로필 테이블중 프로필 테이블에 작업수행이 더 빈번할 것으로 판단되어 프로필에 외래키를 관리하도록 줌.
    private User user;

}
