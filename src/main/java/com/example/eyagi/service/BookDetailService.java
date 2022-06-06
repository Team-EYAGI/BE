package com.example.eyagi.service;


import com.example.eyagi.dto.AudioPreDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Books;
import com.example.eyagi.repository.AudioBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class BookDetailService {

    private final BooksService booksService;
    private final AudioBookRepository audioBookRepository;

    //책 상세 페이지 조회 . 책 정보 + AudioPreDto (오디오 미리듣기에 대한 정보)를 담는다.
    public Map<String, Object> readBookDetail (Long id) {
        Books book = booksService.findBook(id);

        List<AudioBook> audioBooks = book.getAudioBookList();
        List<AudioPreDto> audioPreDtos = new ArrayList<>();
        for (AudioBook a : audioBooks) {
            AudioPreDto dto = AudioPreDto.builder()
                    .audioBookId(a.getId())
                    .previewFile(a.getPreview().getS3FileName())
                    .sellerId(a.getSeller().getId())
                    .sellerName(a.getSeller().getUsername())
                    .sellerImg(a.getSeller().getUserProfile().getUserImage())
                    .contents(a.getContents())
                    .createdAt(a.getPreview().getCreatedAt().toString())
                    .build();
            audioPreDtos.add(dto);
        }
        Map<String, Object> bookDetail = new HashMap<>();
        bookDetail.put("bookId",book.getBookId());
        bookDetail.put("title",book.getTitle());
        bookDetail.put("publisher",book.getPublisher());
        bookDetail.put("author",book.getAuthor());
        bookDetail.put("category",book.getCategory());
        bookDetail.put("bookImg",book.getBookImg());
        bookDetail.put("summary",book.getSummary());
        bookDetail.put("audio",audioPreDtos);

        return bookDetail;
    }

}
