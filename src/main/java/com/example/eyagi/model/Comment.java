package com.example.eyagi.model;


import com.example.eyagi.dto.CommentDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Comment extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    private String content;

    @ManyToOne //양방향
    @JoinColumn(name = "USER_ID")
    private User user;

    /*
    단방향 -> 해당 게시물이 삭제가 되어도, 사용자에게는 기록의 계념으로, 후기가 삭제되지않게 양방향을 걸지 않았다.
    (cascad도 안쓸건데 후기를 굳이 양방향으로 할 이유를 못느낌.)
     */
    @ManyToOne
    @JoinColumn(name = "AUDIOBOOK_ID")
    private AudioBook audioBook;

    public Comment (CommentDto commentDto, User user1, AudioBook audioBook1){
        this.title = commentDto.getTitle();
        this.content = commentDto.getContent();
        this.user = user1;
        this.audioBook = audioBook1;
    }

    public void update(CommentDto commentDto){
        this.title = commentDto.getTitle();
        this.content = commentDto.getContent();
    }


}
