
package com.example.eyagi.service;


import com.example.eyagi.dto.BookRequestDto;
import com.example.eyagi.model.BookRequest;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.BookRequestRepository;
import com.example.eyagi.repository.QRepository.BookRequestCustomRepository;
import com.example.eyagi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BookRequestService {

    private final BookRequestRepository bookRequestRepository;
    private final UserRepository userRepository;


    // 요청목록 등록순으로 불러오기
    public ResponseEntity<?> findAllRequest(Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
        pageable = PageRequest.of(page, pageable.getPageSize(), sort );
        Page<BookRequestCustomRepository> bookRequestPage = bookRequestRepository.findAllByOrderByBookRequestId(pageable);
        List<BookRequestCustomRepository> bookRequestList = bookRequestPage.getContent();
        List<BookRequestDto.ResponesDto> dtoList = new ArrayList<>();
        for (BookRequestCustomRepository b : bookRequestList){
            BookRequestDto.ResponesDto dto = new BookRequestDto.ResponesDto(b.getBookRequestId(), b.getTitle(), b.getContents(), b.getUserName(), b.getCreatedAt(), b.getBookId());
            dtoList.add(dto);
        }
        PageImpl pageImpl = new PageImpl<>(dtoList, pageable, bookRequestPage.getTotalElements());
        return ResponseEntity.ok().body(pageImpl);
    }

    // 저장하기
    public Long save(BookRequestDto bookRequestDto, String userEmail, Long bookId){

        User user = userRepository.findByEmail(userEmail).orElseThrow(()
        -> new NullPointerException("가입되어있지 않은 user입니다."));

        BookRequest bookRequest = new BookRequest(bookRequestDto,user,bookId);
        bookRequestRepository.save(bookRequest);

        return bookRequest.getBookRequestId();
    }

    // 수정하기
    @Transactional
    public void update(Long bookRequestId, BookRequestDto bookRequestDto, User user){
        BookRequest bookRequest = bookRequestRepository.findById(bookRequestId).orElseThrow(()
        -> new NullPointerException("요청글을 찾을 수가 없습니다."));

        if(!bookRequest.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
        bookRequest.update(bookRequestDto);
    }

    //삭제하기
    @Transactional
    public void delete(Long bookRequestId, User user){
        bookRequestRepository.findById(bookRequestId)
                .map(bookRequest -> {
                    if(!bookRequest.getUser().getId().equals(user.getId())){
                        throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
                    }
                    bookRequestRepository.deleteById(bookRequestId);
                    return bookRequest;
                })
                .orElseThrow(()-> new NullPointerException("요청글을 찾지 못했습니다."));
    }
}

