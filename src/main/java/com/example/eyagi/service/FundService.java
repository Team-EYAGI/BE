package com.example.eyagi.service;

import com.example.eyagi.dto.*;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.*;
import com.example.eyagi.repository.QRepository.FundCustomRepository;
import com.example.eyagi.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

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
    public ResponseEntity<?> getAllFund(FundUserRequestDto requestDto, Pageable pageable) {
        boolean myHeartFund;

        //pageable
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
        pageable = PageRequest.of(page, pageable.getPageSize(), sort );
//        boolean successGoals;

        User user = null;
        if(requestDto != null) {
            user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new NullPointerException("유저 X"));
        }
        Page<FundCustomRepository> fundPage = fundRepository.findByOrderByFundId(pageable);
        List<FundCustomRepository> fundList = fundPage.getContent();
        List<FundResponseDto> fundResponse = new ArrayList<>();

        for(FundCustomRepository fundCustomRepository : fundList) {
            // 좋아요 반영해서 myHeart 담아야함.
            myHeartFund = false;
            boolean existsFundHeart = fundHeartRepository.existsByUserAndFund_FundId(user, fundCustomRepository.getFundId());
            if(user != null) {
                if(existsFundHeart) {
                    myHeartFund = true;
                }
            }
            // 펀딩 성공 여부.
//            successGoals = false;
//            if(fund.getHeartCnt() >= fund.getFundingGoals()) {
//                successGoals = true;
//            }
            FundResponseDto fundResponseDto = FundResponseDto.builder()
                    .fundId(fundCustomRepository.getFundId())
                    .sellerName(fundCustomRepository.getSellerName())
                    .likeCnt(fundCustomRepository.getLikeCnt())
                    .bookTitle(fundCustomRepository.getBookTitle())
                    .bookImg(fundCustomRepository.getBookImg())
                    .myHeart(myHeartFund)
                    .successFunding(fundCustomRepository.getSuccessFunding())
                    .build();
            fundResponse.add(fundResponseDto);
        }
        PageImpl pageImpl = new PageImpl<>(fundResponse, pageable, fundPage.getTotalElements());
        return ResponseEntity.ok().body(pageImpl);
    }

    // allfund 비login시
    public ResponseEntity<?> getAllFundByNoUser(Pageable pageable) {
        boolean myHeartFund = false;
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
        pageable = PageRequest.of(page, pageable.getPageSize(), sort );

        Page<FundCustomRepository> fundPage = fundRepository.findByOrderByFundId(pageable);
        List<FundCustomRepository> fundList = fundPage.getContent();
        List<FundResponseDto> fundResponse = new ArrayList<>();

        for(FundCustomRepository fundCustomRepository : fundList) {
            FundResponseDto fundResponseDto = FundResponseDto.builder()
                    .fundId(fundCustomRepository.getFundId())
                    .sellerName(fundCustomRepository.getSellerName())
                    .likeCnt(fundCustomRepository.getLikeCnt())
                    .bookTitle(fundCustomRepository.getBookTitle())
                    .bookImg(fundCustomRepository.getBookImg())
                    .myHeart(myHeartFund)
                    .successFunding(fundCustomRepository.getSuccessFunding())
                    .build();
            fundResponse.add(fundResponseDto);
        }
        PageImpl pageImpl = new PageImpl<>(fundResponse, pageable, fundPage.getTotalElements());
        return ResponseEntity.ok().body(pageImpl);
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
                .successFunding(foundFund.isSuccessGoals())
                .build();


    }

    public ResponseEntity<?> mainFundList() {
        //추천도서 list
        List<Fund> findAllFund = fundRepository.findAllByOrderByFundIdDesc();
        List<SellerFundDto> randomFundList = new ArrayList<>();

        for (Fund fund : findAllFund) {
            SellerFundDto fundMainResponseDto = SellerFundDto.builder()
                    .fundId(fund.getFundId())
                    .sellerName(fund.getUser().getUsername())
                    .likeCnt(fund.getHeartCnt())
//                    .fundFile(fund.getAudioFund().getFundFile())
                    .successFunding(fund.isSuccessGoals())
                    .bookTitle(fund.getBooks().getTitle())
                    .bookImg(fund.getBooks().getBookImg())
                    .build();
            randomFundList.add(fundMainResponseDto);
        }
        Collections.shuffle(randomFundList); //리스트 내 값 랜덤으로 순서 재배치

        // 초기에  개수가 적을시
        List<SellerFundDto> bestFund = new ArrayList<>();
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

    // 펀딩상세보기 - 회원
    public ResponseEntity<?> detailFund(Long fundid, FundUserRequestDto requestDto /* UserDetailsImpl userDetails */) {
        boolean myHeartFund;
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new NullPointerException("유저 X"));
        Fund foundFund = fundRepository.findById(fundid).orElseThrow(
                () -> new NullPointerException("펀딩내용을 찾을 수 없습니다."));

        myHeartFund = false;
        boolean existsFundHeart = fundHeartRepository.existsByUserAndFund(user, foundFund);
        if(user != null) {
            if(existsFundHeart) {
                myHeartFund = true;
            }
        }
        // 상세보기
        FundDetailResponseDto fundDetailResponseDto = FundDetailResponseDto.builder()
                // 저자...
                .fundId(foundFund.getFundId())
                .sellerId(foundFund.getUser().getId())
                .sellerName(foundFund.getUser().getUsername())
                .sellerImg(foundFund.getUser().getUserProfile().getUserImage())
                .introduce(foundFund.getUser().getUserProfile().getIntroduce())
                .bookTitle(foundFund.getBooks().getTitle())
                .author(foundFund.getBooks().getAuthor())
                .bookImg(foundFund.getBooks().getBookImg())
                .fundFile(foundFund.getAudioFund().getFundFile())
                .successFunding(foundFund.isSuccessGoals())
                .fundingGoals(foundFund.getFundingGoals())
                .likeCnt(foundFund.getHeartCnt())
                .content(foundFund.getContent())
                .myHeart(myHeartFund)
                .followerCnt(foundFund.getUser().getFollwerCnt())
                .bookId(foundFund.getBooks().getBookId())
                .category(foundFund.getBooks().getCategory())
                .build();

        //연관 4개
//        List<Fund> fundList = fundRepository.findAll();
//        List<FundResponseDto> fundResponse = new ArrayList<>();
//        for(Fund f : fundList) {
//            myHeartFund = false;
//            existsFundHeart = fundHeartRepository.existsByUserAndFund_FundId(user, f.getFundId());
//            if(user != null) {
//                if(existsFundHeart) {
//                    myHeartFund = true;
//                }
//            }
//            FundResponseDto fundResponseDto = FundResponseDto.builder()
//                    .fundId(f.getFundId())
//                    .sellerName(f.getUser().getUsername())
//                    .likeCnt(f.getHeartCnt())
//                    .fundFile(f.getAudioFund().getFundFile())
//                    .bookTitle(f.getBooks().getTitle())
//                    .bookImg(f.getBooks().getBookImg())
//                    .myHeart(myHeartFund)
//                    .fundingGoals(f.getFundingGoals())
//                    .successFunding(f.isSuccessGoals())
//                    .build();
//            fundResponse.add(fundResponseDto);
//        }


        Map<String, Object> fundDetail = new HashMap<>();
        fundDetail.put("content",fundDetailResponseDto);
        fundDetail.put("ano","디자인 하단 추천페이지용 ");
        return ResponseEntity.ok().body(fundDetail);
    }

    // 펀딩상세보기 - 비회원
    public ResponseEntity<?> detailFundNoUser(Long fundid) {
        boolean myHeartFund;
        Fund foundFund = fundRepository.findById(fundid).orElseThrow(
                () -> new NullPointerException("펀딩내용을 찾을 수 없습니다."));

        myHeartFund = false;
        // 상세보기
        FundDetailResponseDto fundDetailResponseDto = FundDetailResponseDto.builder()
                // 저자...
                .fundId(foundFund.getFundId())
                .sellerId(foundFund.getUser().getId())
                .sellerName(foundFund.getUser().getUsername())
                .sellerImg(foundFund.getUser().getUserProfile().getUserImage())
                .introduce(foundFund.getUser().getUserProfile().getIntroduce())
                .bookTitle(foundFund.getBooks().getTitle())
                .author(foundFund.getBooks().getAuthor())
                .bookImg(foundFund.getBooks().getBookImg())
                .fundFile(foundFund.getAudioFund().getFundFile())
                .successFunding(foundFund.isSuccessGoals())
                .fundingGoals(foundFund.getFundingGoals())
                .likeCnt(foundFund.getHeartCnt())
                .content(foundFund.getContent())
                .myHeart(myHeartFund)
                .followerCnt(foundFund.getUser().getFollwerCnt())
                .bookId(foundFund.getBooks().getBookId())
                .category(foundFund.getBooks().getCategory())
                .build();

        Map<String, Object> fundDetail = new HashMap<>();
        fundDetail.put("content",fundDetailResponseDto);
        fundDetail.put("ano","디자인 하단 추천페이지용 ");
        return ResponseEntity.ok().body(fundDetail);
    }
}
