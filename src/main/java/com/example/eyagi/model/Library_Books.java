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

    @ManyToOne
    @JoinColumn(name = "UserLibrary_ID")
    private UserLibrary userLibrary;

    @ManyToOne
    @JoinColumn(name = "Book_ID")
    private Books Book;

    public Library_Books (UserLibrary library, Books book){
        this.userLibrary = library;
        this.Book = book;
    }


}
