package com.example.eyagi.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; //이메일

    @Column(nullable = false)
    private String username; //이름

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    private String userImage;

    private String originImage;

    @Column(unique = true)
    private Long kakaoId;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.REMOVE)
    private List<AudioBook> audioBookList;



    public User(String email, String username, String password, UserRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void update(String imgurl, String fileName) {
        this.userImage = imgurl;
        this.originImage = fileName;
    }

//    // 현재 유저가 팔로우하는 부분
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userFollowingId")
//    private User userFollowing = this;
//
//    @OneToMany(mappedBy = "userFollowing", cascade = CascadeType.REMOVE)
//    private List<User> followingList = new ArrayList<User>();

//    // 현재 유저가 팔로우 당하는 부분
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userFollowerId")
//    private User userFollower = this;
//
//    @OneToMany(mappedBy = "userFollower", cascade = CascadeType.REMOVE)
//    private List<User> followerList = new ArrayList<User>();
//
//    public void addFollowing(User following) {
//        this.followingList.add(following);
//
//        if(!following.getFollowerList().contains(this)) {
//            following.getFollowerList().add(this);
//        }
//    }
//    public void addFollower(User follower) {
//        this.followerList.add(follower);
//
//        if(follower.getFollowingList().contains(this)) {
//            follower.getFollowingList().add(this);
//        }
//    }
}