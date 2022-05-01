package com.example.eyagi.service;

import com.example.eyagi.dto.FundRequestDto;
import com.example.eyagi.dto.FundResponseDto;
import com.example.eyagi.model.AudioFund;
import com.example.eyagi.model.Books;
import com.example.eyagi.model.Fund;
import com.example.eyagi.model.User;
import com.example.eyagi.repository.AudioFundRepository;
import com.example.eyagi.repository.BooksRepository;
import com.example.eyagi.repository.FundRepository;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundService {
    private final AudioFundRepository audioFundRepository;
    private final FundRepository fundRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;

    public ResponseEntity<?> saveFund(Long BookId, MultipartFile multipartFile, FundRequestDto fundRequestDto, UserDetailsImpl userDetails) {
        String userEmail = userDetails.getUsername();
//        FundValidator.validatePostSaveRegister(fundSaveDto, multipartFile, userEmail);
//        Map<String, String> map = awsS3Service.uploadFile(multipartFile);
        User joinUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("유효한 회원을 찾을 수 없습니다."));
        Books books = booksRepository.findById(BookId).orElseThrow(
                () -> new IllegalArgumentException("등록된 책이 없습니다."));
        // 오디오 따로 저장
        AudioFund audioFund = AudioFund.builder()
//                .fundFile(map.get("url"))
//                .originName(map.get("fileName"))
                .build();
        audioFundRepository.save(audioFund);
        // 펀딩 등록
        Fund fund = new Fund(fundRequestDto, audioFund, joinUser, books);
        fundRepository.save(fund);

        // Id 반환
        Long requestId = fund.getFundId();
        return ResponseEntity.ok().body(requestId);
    }

    public ResponseEntity<?> getAllFund() {
        List<Fund> fundList = fundRepository.findAllByOrderByCreatedAtDesc();
        List<FundResponseDto> fundResponse = new ArrayList<>();
        for(Fund fund : fundList) {
            FundResponseDto fundResponseDto = FundResponseDto.builder()
                    .fundId(fund.getFundId())
                    .sellerName(fund.getUser().getUsername())
                    .content(fund.getContent())
//                    .likeCnt()
                    .fundFile(fund.getAudioFund().getFundFile())
                    .bookTitle(fund.getBooks().getTitle())
                    .author(fund.getBooks().getAuthor())
                    .bookImg(fund.getBooks().getBookImg())
                    .build();
            fundResponse.add(fundResponseDto);
        }
        return ResponseEntity.ok().body(fundList);
    }
}
