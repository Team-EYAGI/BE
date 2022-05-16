package com.example.eyagi.dto;


import com.example.eyagi.model.BookRequest;
import com.example.eyagi.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookRequestDto {

    private String title;
    private String contents;

/*    public BookRequestDto(BookRequest bookRequest){
        this.setTitle(bookRequest.getTitle());
        this.setContents(bookRequest.getContents());
    }*/
    @NoArgsConstructor
    @Getter
    public static class ResponesDto {

        private Long bookRequestId;

        private String title;

        private String contents;

        private String userName;

        private String createdAt; //수정일 -> 등록일로 변경.

        private Long bookId;

        public ResponesDto (BookRequest bookRequest){
            this.bookRequestId = bookRequest.getBookRequestId();
            this.contents = bookRequest.getContents();
            this.title = bookRequest.getTitle();
            this.userName = bookRequest.getUser().getUsername();
            this.bookId = bookRequest.getBookId();
            this.createdAt = bookRequest.getCreatedAt().toString();
        }

    }

}
