package com.example.eyagi.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; //이메일

    @Column(nullable = false, unique = true)
    private String username; //닉네임

    @Column(nullable = false)
    private String password;

    @Column
    private Integer followingCnt; //내가 follow를 하고 있는 사람들

    @Column
    private Integer follwerCnt; //나를 follow를 하고 있는 사람들

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private UserLibrary userLibrary;


    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE) //프로필은 회원이 탈퇴하면 함께 사라짐.
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE) //회원이 단 후기는 회원이 탈퇴하면 함께 사라짐.
    private List<Comment> comments;


    @OneToMany(mappedBy = "seller", cascade = CascadeType.REMOVE) //해당 셀러가 등록한 오디오북은 셀러가 탈퇴하면 함께 사라짐.
    private List<AudioBook> audioBookList;

    @Column(unique = true)
    private Long kakaoId;


    public User(String email, String username, String password, UserRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public User(String email,String username,String password,UserRole role,Long kakaoId){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.kakaoId = kakaoId;
    }


    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    private List<Follow>followingList = new ArrayList<>(); //

    @OneToMany(mappedBy = "followed", fetch = FetchType.LAZY)
    private List<Follow>followedList = new ArrayList<>();


}


