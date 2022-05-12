package com.example.eyagi.service;

import com.example.eyagi.dto.*;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.*;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.eyagi.service.AwsS3Path.pathFund;

@Service
@RequiredArgsConstructor
public class FundService {
    private final AudioFundRepository audioFundRepository;
    private final FundRepository fundRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;
    private final AwsS3Service awsS3Service;
    private final FundHeartRepository fundHeartRepository;

    public ResponseEntity<?> saveFund(Long BookId, MultipartFile multipartFile, FundRequestDto fundRequestDto,
                                      UserDetailsImpl userDetails) {
        // 최소 펀딩 수량 제한 체킹다시.
        if(fundRequestDto.getFundingGoals() < 5) {
            return new ResponseEntity("최소 목표량은 5가 넘어야합니다.", HttpStatus.BAD_REQUEST);
        }

        String userEmail = userDetails.getUsername();
//        FundValidator.validatePostSaveRegister(fundRequestDto, multipartFile, userEmail);
        Map<String, String> map = awsS3Service.uploadFile(multipartFile,pathFund);
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

    // allfund login시
    public ResponseEntity<?> getAllFund(FundUserRequestDto requestDto) {
        boolean myHeartFund;
        boolean successGoals;
        User user = null;
        if(requestDto != null) {
            user = userRepository.findByEmail(requestDto.getUseremail()).orElseThrow(() -> new NullPointerException("유저 X"));
        }
        List<Fund> fundList = fundRepository.findAllByOrderByFundIdDesc();
        List<FundResponseDto> fundResponse = new ArrayList<>();

        for(Fund fund : fundList) {
            // 좋아요 반영해서 myHeart 담아야함.
            myHeartFund = false;
            boolean existsFundHeart = fundHeartRepository.existsByUserAndFund(user, fund);
            System.out.println(existsFundHeart);
            if(user != null) {
                if(existsFundHeart) {
                    myHeartFund = true;
                }
            }
            // 펀딩 성공 여부.
            successGoals = false;
            if(fund.getHeartCnt() >= fund.getFundingGoals()) {
                successGoals = true;
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
                    .fundingGoals(fund.getFundingGoals())
                    .successFunding(successGoals)
                    .build();
            fundResponse.add(fundResponseDto);
        }
        return ResponseEntity.ok().body(fundResponse);
    }

    // allfund 비login시
    public ResponseEntity<?> getAllFundByNoUser() {
        boolean myHeartFund = false;
        boolean successGoals = false;
        List<Fund> fundList = fundRepository.findAllByOrderByFundIdDesc();
        List<FundResponseDto> fundResponse = new ArrayList<>();

        for(Fund fund : fundList) {
            if(fund.getHeartCnt() >= fund.getFundingGoals()) {
                successGoals = true;
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
                    .fundingGoals(fund.getFundingGoals())
                    .successFunding(successGoals)
                    .build();
            fundResponse.add(fundResponseDto);
        }
        return ResponseEntity.ok().body(fundResponse);
    }

    // funding 후원
    @Transactional
    public FundHeartResponseDto saveFundHeart(Long fundid, FundHeartRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Fund foundFund = fundRepository.findById(fundid).orElseThrow(
                () -> new NullPointerException("펀딩내용을 찾을 수 없습니다."));

        boolean existsFundHeart = fundHeartRepository.existsByUserAndFund(user, foundFund);

        // 펀딩 후원 취소
        if(requestDto.isFundHeartBool()) {
            if (existsFundHeart) {
                throw new NullPointerException("펀딩을 후원한 적이 있습니다.");
            }
            FundHeart fundHeart = FundHeart.builder()
                    .fund(foundFund)
                    .user(user)
                    .build();
            fundHeartRepository.save(fundHeart);

        // 펀딩 후원 하기
        } else { // false
            if (!existsFundHeart) {
                throw new NullPointerException("펀딩 후원 내역을 찾을 수 없습니다.");
            }
            fundHeartRepository.deleteByUserAndFund(user, foundFund);
        }
        // 펀딩응원 수 늘리기
        foundFund.updateHeartCnt(requestDto.isFundHeartBool());

        // 여기
        return FundHeartResponseDto.builder()
                .fundHeartBool(requestDto.isFundHeartBool())
                .fundHeartCnt(foundFund.getHeartCnt())
                .build();


    }

    public ResponseEntity<?> mainFundList() {
    /*    Long qty = fundRepository.countAll();
        int idx = (int)(Math.random() * qty);
        boolean myHeartFund;
        List<Fund> setFundList = new ArrayList<>();
        List<FundResponseDto> fundResponse = new ArrayList<>();
        // 데이터 개수 지정 5
        Page<Fund> fundMainPage = fundRepository.findAll(PageRequest.of(idx, 1));
        if(fundMainPage.hasContent()) {
            setFundList.add(fundMainPage.getContent().get(0));
        }

        for(Fund fund : setFundList) {
            myHeartFund = false;
            FundResponseDto fundResponseDto = FundResponseDto.builder()
                    .fundId(fund.getFundId())
                    .sellerName(fund.getUser().getUsername())
                    .fundFile(fund.getAudioFund().getFundFile())
                    .bookTitle(fund.getBooks().getTitle())
                    .bookImg(fund.getBooks().getBookImg())
                    .myHeart(myHeartFund)
                    .build();
            fundResponse.add(fundResponseDto);
        }
        return ResponseEntity.ok().body(fundResponse);
    }
    */
        //추천도서 list
        List<Fund> findAllFund = fundRepository.findAllByOrderByFundIdDesc();
        List<FundMainResponseDto> randomFundList = new ArrayList<>();

        for (Fund fund : findAllFund) {
            FundMainResponseDto fundMainResponseDto = FundMainResponseDto.builder()
                    .fundId(fund.getFundId())
                    .sellerName(fund.getUser().getUsername())
                    .likeCnt(fund.getHeartCnt())
                    .fundFile(fund.getAudioFund().getFundFile())
                    .bookTitle(fund.getBooks().getTitle())
                    .bookImg(fund.getBooks().getBookImg())
                    .build();
            randomFundList.add(fundMainResponseDto);
        }
        Collections.shuffle(randomFundList); //리스트 내 값 랜덤으로 순서 재배치

        // 초기에  개수가 적을시
        List<FundMainResponseDto> bestFund = new ArrayList<>();
        if(randomFundList.size() < 5) {
            for (int i = 0; i < randomFundList.size(); i++) {
                bestFund.add(randomFundList.get(i));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                bestFund.add(randomFundList.get(i));
            }
        }

        return ResponseEntity.ok().body(bestFund);
    }
}
