
package com.example.eyagi.model;

import com.example.eyagi.dto.BookRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BookRequest extends Timestamped {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookRequestId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    // 요청생성 생성자
    public BookRequest(BookRequestDto bookRequestDto, User user){
        this.title = bookRequestDto.getTitle();
        this.contents = bookRequestDto.getContents();
        this.user = user;
    }


    public void update(BookRequestDto bookRequestDto){
        this.title = bookRequestDto.getTitle();
        this.contents = bookRequestDto.getContents();
    }

}

