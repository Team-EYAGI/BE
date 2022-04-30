
package com.example.eyagi.service;


import com.example.eyagi.dto.BookRequestDto;
import com.example.eyagi.model.BookRequest;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.BookRequestRepository;
import com.example.eyagi.repository.BooksRepository;
import com.example.eyagi.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class BookRequestService {

    private final BookRequestRepository bookRequestRepository;
    private final BooksRepository booksRepository;
    private final UserRepository userRepository;


    // 요청목록 등록순으로 불러오기
    public List<BookRequest> findAllRequest(){
        return bookRequestRepository.findAllByOrderByModifiedAtAsc();
    }

    // 저장하기
    public BookRequest save(BookRequestDto bookRequestDto, String userEmail){

        User user = userRepository.findByEmail(userEmail).orElseThrow(()
        -> new NullPointerException("가입되어있지 않은 user입니다."));

        //사용자가 이미 요청을 했는지 여부
        boolean exists = bookRequestRepository.findByUserEmail(user.getEmail()).isPresent();
        if(exists){
            throw new NullPointerException("이미 요청이 등록되어있습니다.");
        }
        BookRequest bookRequest = new BookRequest(bookRequestDto,user);

        return bookRequestRepository.save(bookRequest);
    }

    // 수정하기
    public void update(Long bookRequestId, BookRequestDto bookRequestDto){
        BookRequest bookRequest = bookRequestRepository.findById(bookRequestId).orElseThrow(()
        -> new NullPointerException("요청글을 찾을 수가 없습니다."));

        bookRequest.update(bookRequestDto);
    }

    //삭제하기
//    public void delete(Long bookRequestId){
//        bookRequestRepository.findById(bookRequestId)
//                .map(bookRequest -> {
//                    bookRequestRepository.deleteById(bookRequestId);
//                    return bookRequest;
//                })
//                .orElseThrow(()-> new NotFoundException("요청글을 찾지 못했습니다."));
//    }


}

