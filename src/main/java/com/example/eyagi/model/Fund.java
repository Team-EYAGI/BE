package com.example.eyagi.model;

import com.example.eyagi.dto.FundRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Fund extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundId;

    @Column
    private String title;

    @Column
    private String content;

    @OneToOne
    @JoinColumn(name = "fundAudioId")
    private AudioFund audioFund;

    @ManyToOne
    private User user;

    @ManyToOne
    private Books books;

    public Fund(FundRequestDto fundRequestDto, AudioFund audioFund , User user, Books books) {
        this.title = fundRequestDto.getTitle();
        this.content = fundRequestDto.getContent();
        this.user = user;
        this.audioFund = audioFund;
        this.books = books;
    }
}
