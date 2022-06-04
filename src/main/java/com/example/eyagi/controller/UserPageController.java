package com.example.eyagi.controller;

import com.example.eyagi.Interceptor.Auth;
import com.example.eyagi.dto.*;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.service.AwsS3Service;
import com.example.eyagi.service.UserPageService;
import com.example.eyagi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/load/profiles")
@RequiredArgsConstructor
@RestController
public class UserPageController {

    private final UserPageService userPageService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;


      //마이페이지 조회 - 해야됨

    //마이페이지 조회 .1 페이지 로드 시 필요한 것, 판매자는 음성도 같이.- 포스트맨 테스트 완료
    @GetMapping("/")
    public ResponseEntity<UserPageDto> loadUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        if(user.getRole()==UserRole.SELLER){
            return ResponseEntity.ok( userPageService.loadSellerPage(user));
        } else{
            return ResponseEntity.ok( userPageService.loadUserPage(user));
        }
    }

    //마이페이지 조회 .2-1 내 서재 불러오기 , 서재에 담은 책 - 포스트맨 테스트 완료
    @GetMapping("/library/book")
    public ResponseEntity<List<BooksDto>> loadUserLibraryBook(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return ResponseEntity.ok(userPageService.loadMyLibraryBooks(user));
    }

    //마이페이지 조회 .2-1-1 내 서재 불러오기 , 서재에 담은 책 > 책 목록에서 "삭제" - 포스트맨 테스트 완료
    @DeleteMapping("/library/book/{bookId}/remove")
    public ResponseEntity<String> removeLibraryBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long id = userPageService.removeLibraryBook(bookId, userDetails.getUser());
        return ResponseEntity.ok("서재에서 "+ id + "번 도서가 삭제되었습니다");
    }


    //마이페이지 조회 .2-2 내 서재 불러오기 , 듣고 있는 오디오 목록 - 포스트맨 테스트 완료
    @GetMapping("/library/audio")
    public ResponseEntity<List<LibraryAudiosDto>> loadUserLibraryAudio(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return ResponseEntity.ok(userPageService.loadMyLibraryAudios(user));
    }


    //마이페이지 조회 .2-2-1 내 서재 불러오기 , 듣고 있는 오디오 목록 > 목록에서 오디오 "삭제" - 포스트맨 테스트 완료
    @DeleteMapping("/library/audio/{audioId}/remove")
    public ResponseEntity<String> removeLibraryAudio(@PathVariable Long audioId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long id = userPageService.removeLibraryAudio(audioId,userDetails.getUser());
        return ResponseEntity.ok("서재에서 "+ id + "번 오디오북이 삭제되었습니다");
    }



    //마이페이지 조회 .3-1 판매자 전용 버튼, 내가 등록한 오디오북 - 포스트맨 테스트 완료
    @Auth
    @GetMapping("/seller/audioBook")
    public ResponseEntity<List<SellerAudioBook>> loadSellerPageMyAudioBook(@AuthenticationPrincipal UserDetailsImpl userDetails){
       User seller = userDetails.getUser();
       return ResponseEntity.ok(userPageService.sellerMyAudioBook(seller));
    }


    //마이페이지 조회 .3-2 판매자 전용 버튼, 내가 등록한 펀딩 목록
    @Auth
    @GetMapping("/seller/fund")
    public ResponseEntity<List<SellerFundDto>>  loadSellerPageMyFund(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User seller = userDetails.getUser();
        return ResponseEntity.ok(userPageService.myFund(seller));
    }


    //todo: Not MVP 일반 사용자 마이페이지에 내가 참여한 펀딩 목록이 있으면 좋을 것 같다.
    @GetMapping("/joinFund")
    public void loadUserJoinFund(){

    }




}
