package com.example.eyagi.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Follow {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following")
    private User following;

    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower;


}

