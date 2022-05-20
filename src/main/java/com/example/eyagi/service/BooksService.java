
package com.example.eyagi.service;


import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.model.AudioBook;
import com.example.eyagi.model.Books;
import com.example.eyagi.repository.AudioBookRepository;
import com.example.eyagi.repository.BooksRepository;
import com.example.eyagi.repository.QRepository.BooksCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final AudioBookRepository audioBookRepository;


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
    public ResponseEntity<?> findBooksByCategory(String category, Pageable pageable) {
        //pageable
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "bookId" );
        pageable = PageRequest.of(page, pageable.getPageSize(), sort );

        Page<BooksCustomRepository> findBookPage = booksRepository.findByCategoryAndOrderByBookId(category,pageable);
        List<BooksCustomRepository> findBookList = findBookPage.getContent();
        List<BooksDto> bookList = new ArrayList<>();

        for (BooksCustomRepository bCR : findBookList) {
            BooksDto booksDto = new BooksDto(bCR.getBookId(), bCR.getBookImg(), bCR.getTitle(), bCR.getPublisher(), bCR.getAuthor(), bCR.getCategory());
            bookList.add(booksDto);
        }
        PageImpl pageImpl = new PageImpl(bookList, pageable, findBookPage.getTotalElements());
        return ResponseEntity.ok().body(pageImpl);
    }

//    //카테고리 별 책리스트 가져오기
//    public List<BooksDto> findBooksByCategory(String category) {
//        List<Books> findBookList = booksRepository.findByCategory(category);
//        List<BooksDto> bookList = new ArrayList<>();
//
//        for (Books books : findBookList) {
//            BooksDto booksDto = new BooksDto(books);
//            bookList.add(booksDto);
//        }
//        return bookList;
//    }


//// 메인 (추천도서)+(자기계발서)
//    public Map<String, Object> showMainBooks(){
//
//        //추천도서 list
//        List<Books>findAllBook = booksRepository.findAll();
//        List<BooksDto>randomBookList = new ArrayList<>();
//
//        for(Books books : findAllBook){
//            BooksDto booksDto = BooksDto.builder()
//                    .bookId(books.getBookId())
//                    .title(books.getTitle())
//                    .author(books.getAuthor())
//                    .publisher(books.getPublisher())
//                    .bookImg(books.getBookImg())
//                    .category(books.getCategory())
//                    .build();
//            randomBookList.add(booksDto);
//        }
//        Collections.shuffle(randomBookList); //리스트 내 값 랜덤으로 순서 재배치
//
//        List<BooksDto>BestBooks = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            BestBooks.add(randomBookList.get(i));
//        }
//
//        // 자기계발서 책 list
//        String category = "self";
//        List<Books> findSelfList = booksRepository.findByCategory(category);
//        List<BooksDto> allSelfList = new ArrayList<>();
//        for (Books books : findSelfList) {
//            BooksDto booksDto = BooksDto.builder()
//                    .bookId(books.getBookId())
//                    .title(books.getTitle())
//                    .author(books.getAuthor())
//                    .publisher(books.getPublisher())
//                    .bookImg(books.getBookImg())
//                    .category(books.getCategory())
//                    .build();
//            allSelfList.add(booksDto);
//        }
//        Collections.shuffle(allSelfList);
//        List<BooksDto> selfList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            selfList.add(allSelfList.get(i));
//        }
//        Map<String, Object> ShowBookLists = new HashMap<>();
//        ShowBookLists.put("BestBook", BestBooks);
//        ShowBookLists.put("self", selfList);
//
//        return ShowBookLists;
//
//    }


//    // 메인 (추천도서)
//    public  List<BooksDto> showMainBooks() {
//        List<AudioBook> findAllAudioBook = audioBookRepository.findAll();
//        //추천도서 list
//        List<Books> findAllBook = booksRepository.findAll();
//        List<BooksDto> randomBookList = new ArrayList<>();
//
//        for (Books books : findAllBook) {
//            BooksDto booksDto = new BooksDto(books);
//            randomBookList.add(booksDto);
//        }
//        Collections.shuffle(randomBookList); //리스트 내 값 랜덤으로 순서 재배치
//
//        List<BooksDto> bestBooks = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            bestBooks.add(randomBookList.get(i));
//        }
//
//        return bestBooks;
//    }

    // 메인 (추천도서)
    public  List<BooksDto> showMainBooks() {
        // 겹치는 것 제외 됬는지 확인.
        List<AudioBook> findAllAudioInBook = audioBookRepository.findAll();
        List<Books> findAllBook = booksRepository.findAll();
        List<BooksDto> randomBookList = new ArrayList<>();
        List<BooksDto> bestBooks = new ArrayList<>();

        // 오디오북있는 책만.!
        for (AudioBook audioBook : findAllAudioInBook) {
            BooksDto booksDto = new BooksDto(audioBook.getBook());
            bestBooks.add(booksDto);
        }
        int poor = 10 - bestBooks.size();

        Collections.shuffle(bestBooks);

        // 오디오북 책이 10개 미만일떄.
        if(bestBooks.size() < 10) {
            for (Books books : findAllBook) {
                BooksDto booksDto = new BooksDto(books);
                randomBookList.add(booksDto);
            }

            Collections.shuffle(randomBookList);

            for (int i = 0; i < poor; i++) {
                bestBooks.add(randomBookList.get(i));
            }
        }

        return bestBooks;
    }


    // 자기계발서 책 list
    public List<BooksDto> mainSeifList () {
        String category = "self";
        List<Books> findSelfList = booksRepository.findByCategory(category);
        List<BooksDto> allSelfList = new ArrayList<>();
        for (Books books : findSelfList) {
            BooksDto booksDto = new BooksDto(books);
            allSelfList.add(booksDto);
        }
        Collections.shuffle(allSelfList);
        List<BooksDto> selfList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            selfList.add(allSelfList.get(i));
        }

        return selfList;
    }


}