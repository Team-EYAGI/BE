package com.example.eyagi.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Follow {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "following")
//    private User following;  //사용자 ->  판매자  //사용자 //user
//
//    @ManyToOne
//    @JoinColumn(name = "follower")
//    private User follower; //판매자의 팔로워들 .. //판매자 //seller
//

}

