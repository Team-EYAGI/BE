package com.example.eyagi.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class AudioBook extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne //양방향
    @JoinColumn(name = "BOOK_ID")
    private Books book;

    @ManyToOne //양방향
    @JoinColumn(name = "SELLER_ID")
    private User seller;

    //챕터별 오디오북 리스트
    @OneToMany(mappedBy = "audioBook", cascade = CascadeType.REMOVE)// 양방향
    private List<AudioFile> audioFile;

    //해당 셀러의 오디오북 미리듣기 . (한권의 오디오북에는 하나의 미리듣기만 존재 => oneToone)
    @OneToOne //단방향
    @JoinColumn(name = "PREVIEW_ID")
    private AudioPreview preview;

    private String contents; // 오디오북 첫 개시때 소개글 .

//    private Heart heart;


    public void addAudio (AudioFile audio) {
         this.getAudioFile().add(audio);
    }

}
