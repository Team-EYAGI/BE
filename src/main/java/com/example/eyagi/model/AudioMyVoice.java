//package com.example.eyagi.model;
//
//
//import lombok.Getter;
//
//import javax.persistence.*;
//
//@Getter
//@Entity
//public class AudioMyVoice {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Long id;
//
//    @Column(nullable = false)
//    private String s3FileName; //S3 경로
//
//    @Column(nullable = false)
//    private String originName; //원본 파일명
//
//    @OneToOne() //판매자 프로필 테이블과 조인. cascade 걸어서 판매자 프로필이 생성됨과 동시에 여기도 생성될 수 있도록 해주자 !!
//
//}
