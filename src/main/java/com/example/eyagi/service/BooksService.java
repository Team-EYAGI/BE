
package com.example.eyagi.service;


import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.model.Books;
import com.example.eyagi.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BooksService {

    private final BooksRepository booksRepository;

    public Books findBook (Long id) {
        return booksRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 책의 페이지를 요청하였습니다.")
        );
    }


    //모든 책 가져오기
    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    //카테고리 별 책리스트 가져오기
    public List<BooksDto> findBooksByCategory(String category) {
        List<Books> findBookList = booksRepository.findByCategory(category);
        List<BooksDto> bookList = new ArrayList<>();

        for (Books books : findBookList) {
            BooksDto booksDto = BooksDto.builder()
                    .bookId(books.getBookId())
                    .title(books.getTitle())
                    .author(books.getAuthor())
                    .publisher(books.getPublisher())
                    .bookImg(books.getBookImg())
                    .category(books.getCategory())
                    .build();

            bookList.add(booksDto);
        }
        return bookList;
    }


// 메인 (추천도서)+(자기계발서)
    public Map<String, Object> showMainBooks(){

        //추천도서 list
        List<Books>findAllBook = booksRepository.findAll();
        List<BooksDto>randomBookList = new ArrayList<>();

        for(Books books : findAllBook){
            BooksDto booksDto = BooksDto.builder()
                    .bookId(books.getBookId())
                    .title(books.getTitle())
                    .author(books.getAuthor())
                    .publisher(books.getPublisher())
                    .bookImg(books.getBookImg())
                    .category(books.getCategory())
                    .build();
            randomBookList.add(booksDto);
        }
        Collections.shuffle(randomBookList); //리스트 내 값 랜덤으로 순서 재배치

        List<BooksDto>BestBooks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            BestBooks.add(randomBookList.get(i));
        }

        // 자기계발서 책 list
        String category = "self";
        List<Books> findSelfList = booksRepository.findByCategory(category);
        List<BooksDto> allSelfList = new ArrayList<>();
        for (Books books : findSelfList) {
            BooksDto booksDto = BooksDto.builder()
                    .bookId(books.getBookId())
                    .title(books.getTitle())
                    .author(books.getAuthor())
                    .publisher(books.getPublisher())
                    .bookImg(books.getBookImg())
                    .category(books.getCategory())
                    .build();
            allSelfList.add(booksDto);
        }
        Collections.shuffle(allSelfList);
        List<BooksDto> selfList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            selfList.add(allSelfList.get(i));
        }
        Map<String, Object> ShowBookLists = new HashMap<>();
        ShowBookLists.put("BestBook", BestBooks);
        ShowBookLists.put("self", selfList);

        return ShowBookLists;

    }



}