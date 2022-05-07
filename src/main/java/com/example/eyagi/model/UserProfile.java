package com.example.eyagi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @OneToOne //이거는 필드는 리팩토링할때 없애야함.
    @JoinColumn(name = "AUDIOFILE_ID")
    private AudioFile userVoice; //내 보이스 - 판매자만 파일 크기 지정해줘야함

    @OneToOne
    @JoinColumn(name = "USER_ID") //유저테이블과 프로필 테이블중 프로필 테이블에 작업수행이 더 빈번할 것으로 판단되어 프로필에 외래키를 관리하도록 줌.
    private User user;

    public UserProfile (User user){
        this.user = user;
    }

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
