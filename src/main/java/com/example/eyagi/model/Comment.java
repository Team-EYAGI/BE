package com.example.eyagi.model;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String content;

//    @ManyToOne //단방향
//    @JoinColumn(name = "USER_ID")
//    private User user;
//
//    @ManyToOne //단방향
//    @JoinColumn(name = "AUDIOBOOK_ID")
//    private AudioBook audioBook;


}
