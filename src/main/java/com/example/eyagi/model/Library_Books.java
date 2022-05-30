package com.example.eyagi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Library_Books {   //다대다 관계를 위한 중간 테이블
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserLibrary_ID")
    private UserLibrary userLibrary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Book_ID")
    private Books book;

    public Library_Books (UserLibrary library, Books book){
        this.userLibrary = library;
        this.book = book;
    }


}
