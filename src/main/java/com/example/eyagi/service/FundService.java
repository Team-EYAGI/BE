package com.example.eyagi.service;

import com.example.eyagi.aws.AwsS3Service;
import com.example.eyagi.dto.FundHeartRequestDto;
import com.example.eyagi.dto.FundHeartResponseDto;
import com.example.eyagi.dto.FundRequestDto;
import com.example.eyagi.dto.FundResponseDto;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.*;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FundService {
    private final AudioFundRepository audioFundRepository;
    private final FundRepository fundRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;
    private final AwsS3Service awsS3Service;
    private final FundHeartRepository fundHeartRepository;


    public ResponseEntity<?> saveFund(Long BookId, MultipartFile multipartFile, FundRequestDto fundRequestDto, UserDetailsImpl userDetails) {
        String userEmail = userDetails.getUsername();
//        FundValidator.validatePostSaveRegister(fundRequestDto, multipartFile, userEmail);
        Map<String, String> map = awsS3Service.uploadFile(multipartFile);
        User joinUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("유효한 회원을 찾을 수 없습니다."));
        Books books = booksRepository.findById(BookId).orElseThrow(
                () -> new IllegalArgumentException("등록된 책이 없습니다."));
        // 오디오 따로 저장
        AudioFund audioFund = AudioFund.builder()
                .fundFile(map.get("url"))
                .originName(map.get("fileName"))
                .build();
        audioFundRepository.save(audioFund);
        // 펀딩 등록
        Fund fund = new Fund(fundRequestDto, audioFund, joinUser, books);
        fundRepository.save(fund);

        // Id 반환
        Long requestId = fund.getFundId();
        return ResponseEntity.ok().body(requestId);
    }

    public ResponseEntity<?> getAllFund(UserDetailsImpl userDetails) {
        boolean myHeartFund;
        User user = null;
        if(userDetails != null) {
            user = userDetails.getUser();
        }
        List<Fund> fundList = fundRepository.findAllByOrderByCreatedAtDesc();
        List<FundResponseDto> fundResponse = new ArrayList<>();

        for(Fund fund : fundList) {
            // 좋아요 반영해서 myHeart 담아야함.
            myHeartFund = false;
            boolean existsFundHeart = fundHeartRepository.existsByUserAndFund(user, fund);
            if(user != null) {
                if(existsFundHeart) {
                    myHeartFund = true;
                }
            }
            FundResponseDto fundResponseDto = FundResponseDto.builder()
                    .fundId(fund.getFundId())
                    .sellerName(fund.getUser().getUsername())
                    .content(fund.getContent())
                    .likeCnt(fund.getHeartCnt())
                    .fundFile(fund.getAudioFund().getFundFile())
                    .bookTitle(fund.getBooks().getTitle())
                    .author(fund.getBooks().getAuthor())
                    .bookImg(fund.getBooks().getBookImg())
                    .myHeart(myHeartFund)
                    .build();
            fundResponse.add(fundResponseDto);
        }
        return ResponseEntity.ok().body(fundResponse);
    }

    // funding 응원
    @Transactional
    public FundHeartResponseDto saveFundHeart(Long fundid, FundHeartRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Fund foundFund = fundRepository.findById(fundid).orElseThrow(
                () -> new NullPointerException("펀딩내용을 찾을 수 없습니다."));

        boolean existsFundHeart = fundHeartRepository.existsByUserAndFund(user, foundFund);

        // 펀딩 후원 취소
        if(requestDto.isFundHeartBool()) {
            if (existsFundHeart) {
                throw new NullPointerException("펀딩을 후원한적없음");
            }
            FundHeart fundHeart = FundHeart.builder()
                    .fund(foundFund)
                    .user(user)
                    .build();
            fundHeartRepository.save(fundHeart);

        // 펀딩 후원 하기
        } else { // false
            if (!existsFundHeart) {
                throw new NullPointerException("펀딩을 후원한 적이 있습니다.");
            }
            fundHeartRepository.deleteByUserAndFund(user, foundFund);
        }
        // 펀딩응원 수 늘리기
        foundFund.updateHeartCnt(requestDto.isFundHeartBool());

        return FundHeartResponseDto.builder()
                .fundHeartBool(requestDto.isFundHeartBool())
                .fundHeartCnt(foundFund.getHeartCnt())
                .build();
    }
}
