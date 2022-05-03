package com.example.eyagi.service;


import com.example.eyagi.dto.AudioPreDto;
import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Books;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookDetailService {

    private final BooksService booksService;

    //책 상세 페이지 조회 . 책 정보 + AudioPreDto (오디오 미리듣기에 대한 정보)를 담는다.
    public BooksDto readBookDetail (Long id) {
        Books book = booksService.findBook(id);

        List<AudioBook> audioBooks = book.getAudioBookList();
        List<AudioPreDto> audioPreDtos = new ArrayList<>();

        for (AudioBook a : audioBooks){
            AudioPreDto dto = AudioPreDto.builder()
                    .audioBookId(a.getId())
                    .previewFile(a.getPreview().getS3FileName())
                    .sellerId(a.getSeller().getId())
                    .sellerName(a.getSeller().getUsername())
                    .sellerImg(a.getSeller().getUserImage())
                    .contents(a.getContents())
//                    .totalHeart(a.) 찜 갯수 해야됨.
                    .build();
            audioPreDtos.add(dto);
        }
        return BooksDto.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .bookImg(book.getBookImg())
                .category(book.getCategory())
                .summary(book.getSummary())
                .audioPreDtoList(audioPreDtos)
                .build();
    }

}
