package com.example.eyagi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class UserProfile extends Timestamped{
// UserProfile 을 상속받는 셀러 프로파일을 만들자 !!!!
// 셀러로 변환해주는 api 만들어서 , 그게 돌아가는 순간 프로필을 바꿔주면 될 것 같다
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String userImage;  //이미지 S3 경로

    private String originImage;  //이미지 파일 이름.

    private String introduce;  //자기소개글. - 판매자만

    private String s3FileName; //오디오 파일 s3 경로

    private String originName; //오디오 파일명.

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUDIOFILE_ID")
    private AudioFile userVoice; //내 보이스 - 판매자만 파일 크기 지정해줘야함


    public void editProfile(String userImage, String originImage){
        this.userImage =userImage;
        this.originImage = originImage;
    }

    public void editSellerProfile(String userImage, String originImage, String introduce){
        this.userImage =userImage;
        this.originImage = originImage;
        this.introduce = introduce;
    }

    public void addMyVoice(String s3, String ori){
        this.s3FileName = s3;
        this.originName = ori;
    }
}
