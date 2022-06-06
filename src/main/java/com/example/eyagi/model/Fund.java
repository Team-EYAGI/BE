package com.example.eyagi.model;

import com.example.eyagi.dto.FundRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static javax.persistence.FetchType.*;
import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(indexes = @Index(name = "i_fund", columnList = "fundId"))
public class Fund extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundId;

    @Column
    private String content;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "fundAudioId")
    private AudioFund audioFund;

    @ManyToOne(fetch = LAZY)
    private User user;

    @ManyToOne(fetch = LAZY)
    private Books books;

    @Column
    private int heartCnt;

    @Column
    private int fundingGoals;

    private boolean successGoals = false;

    public Fund(FundRequestDto fundRequestDto, AudioFund audioFund , User user, Books books) {
        this.content = fundRequestDto.getContent();
        this.user = user;
        this.audioFund = audioFund;
        this.books = books;
        this.heartCnt = 0;
        this.fundingGoals = fundRequestDto.getFundingGoals();
    }

    public void updateHeartCnt(boolean fundHeartBool) {
        if(fundHeartBool) {
            this.heartCnt += 1;
        } else {
            this.heartCnt -= 1;
        }
        successGoalsStatus(this.fundingGoals, this.heartCnt);
    }

    public void successGoalsStatus (int fundingGoals, int heartCnt){
        if (heartCnt >= fundingGoals){
            this.successGoals = true;
        }
    }
}
