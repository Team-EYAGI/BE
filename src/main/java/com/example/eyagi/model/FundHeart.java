package com.example.eyagi.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class FundHeart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="fundId")
    private Fund fund;

    @ManyToOne
    @JoinColumn(name= "userId")
    private User user;

    @Builder
    public FundHeart(Fund fund, User user) {
        this.fund = fund;
        this.user = user;
    }
}
