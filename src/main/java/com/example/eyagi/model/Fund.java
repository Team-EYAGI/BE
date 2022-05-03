package com.example.eyagi.model;

import com.example.eyagi.dto.FundRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Fund extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundId;

    @Column
    private String content;

    @OneToOne
    @JoinColumn(name = "fundAudioId")
    private AudioFund audioFund;

    @ManyToOne
    private User user;

    @ManyToOne
    private Books books;

    @Column
    private int heartCnt;

    public Fund(FundRequestDto fundRequestDto, AudioFund audioFund , User user, Books books) {
        this.content = fundRequestDto.getContent();
        this.user = user;
        this.audioFund = audioFund;
        this.books = books;
        this.heartCnt = 0;
    }

    public void updateHeartCnt(boolean fundHeartBool) {
        if(fundHeartBool) {
            this.heartCnt += 1;
        } else {
            this.heartCnt -= 1;
        }
    }
}
