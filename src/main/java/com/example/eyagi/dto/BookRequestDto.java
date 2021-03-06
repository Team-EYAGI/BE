package com.example.eyagi.dto;


import com.example.eyagi.model.BookRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookRequestDto {

    private String title;
    private String contents;


    @NoArgsConstructor
    @Getter
    public static class ResponesDto {

        private Long bookRequestId;

        private String title;

        private String contents;

        private String userName;

        private LocalDateTime createdAt; //수정일 -> 등록일로 변경.

        private Long bookId;

        public ResponesDto (BookRequest bookRequest){
            this.bookRequestId = bookRequest.getBookRequestId();
            this.contents = bookRequest.getContents();
            this.title = bookRequest.getTitle();
            this.userName = bookRequest.getUser().getUsername();
            this.bookId = bookRequest.getBookId();
            this.createdAt = bookRequest.getCreatedAt();
        }

        public ResponesDto (Long bookRequestId, String title, String contents, String userName, LocalDateTime createdAt, Long bookId){
            this.bookRequestId = bookRequestId;
            this.contents = contents;
            this.title = title;
            this.userName = userName;
            this.bookId = bookId;
            this.createdAt = createdAt;
        }
    }

}
