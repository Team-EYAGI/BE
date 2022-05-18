package com.example.eyagi.service;

import com.example.eyagi.dto.*;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.example.eyagi.service.AwsS3Path.pathImage;
import static com.example.eyagi.service.AwsS3Path.pathInfo;

@RequiredArgsConstructor
@Service
public class UserPageService {

    private final UserProfileRepository userProfileRepository;
    private final UserLibraryRepository userLibraryRepository;
    private final Library_AudioRepository library_audioRepository;
    private final Library_BooksRepository library_booksRepository;
    private final AwsS3Service awsS3Service;
    private final UserService userService;
    private final BooksService booksService;
    private final AudioService audioService;
    private final FundRepository fundRepository;
    private final FollowRepository followRepository;
    private final FollowService followService;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //마이페이지 첫 로드 시 내려주는 데이터 / 유저 닉네임, 유저 이메일, 프로필 이미지, 판매자인 경우 녹음 파일
    public UserPageDto loadUserPage(User user) {
        return new UserPageDto(user);
    }

    public SellerPageDto loadSellerPage(User seller) {
        return new SellerPageDto(seller);
    }

    //듣고 있는 오디오북에 추가
    public void listenBook (AudioBook audioBook, User user){

        UserLibrary library = userLibraryRepository.findByUserId(user.getId());
//        if (library == null){
//            UserLibrary userLibrary = new UserLibrary(user);
//            Library_Audio library_audio = new Library_Audio(userLibrary,audioBook);
//            userLibrary.addAuidoBook(library_audio);
//            userLibraryRepository.save(userLibrary);
//            user.addLibrary(userLibrary);
//            library_audioRepository.save(library_audio);
//        } else {
            Library_Audio library_audio = new Library_Audio(library,audioBook);
            library.addAuidoBook(library_audio);
            library_audioRepository.save(library_audio);
            userLibraryRepository.save(library);
//        }
    }

    //내 서재에 책 담기
    public String heartBook(String email, Long id){
        User user = userService.findUser(email);
        Books books = booksService.findBook(id);
        UserLibrary library = userLibraryRepository.findByUserId(user.getId());
//        if (library == null){
//            UserLibrary userLibrary = new UserLibrary(user);
//            Library_Books library_books = new Library_Books(userLibrary, books);
//            userLibrary.addBook(library_books);
//            userLibraryRepository.save(userLibrary);
//            user.addLibrary(userLibrary);
//            library_booksRepository.save(library_books);
//
//        } else {
        Library_Books bookCheck = library_booksRepository.findByBookAndUserLibrary(books, library);
        if(bookCheck != null){
           return "이미 등록된 도서입니다.";
        } else {
            Library_Books library_books = new Library_Books(library, books);
            library.addBook(library_books);
            library_booksRepository.save(library_books);
            userLibraryRepository.save(library);
            return "도서가 서재에 쏙 담겼습니다!";
        }
    }

    //서재 불러오기 1. 내 서재에 담긴 책 목록
    public List<BooksDto> loadMyLibraryBooks(User user){
        //키값 역순 리스트
        List<Library_Books> library_booksList = library_booksRepository.findAllByUserLibraryOrderByIdDesc(user.getUserLibrary());
//        List<Library_Books> library_booksList = user.getUserLibrary().getMyBook(); //담은 도서.
        List<BooksDto> booksDtoList = new ArrayList<>();
        for(Library_Books lb : library_booksList){
            BooksDto booksDto = new BooksDto(lb.getBook());
            booksDtoList.add(booksDto);
        }
        return booksDtoList;
    }

/*
책제목, 책이미지, 책 아이디, 카테고리, 저자이름, 크리에이터 이름, 오디오북 아이디
 */
    //서재 불러오기 2. 내가 듣고 있는 오디오북
    public List<LibraryAudiosDto> loadMyLibraryAudios(User user){
//        List<Library_Audio> library_audioList = user.getUserLibrary().getMyAuidoBook(); //듣고 있는 오디오북.
        List<Library_Audio> library_audioList =library_audioRepository.findAllByUserLibraryOrderByIdDesc(user.getUserLibrary());
        List<LibraryAudiosDto> libraryAudiosDtoList = new ArrayList<>();
            for(Library_Audio la : library_audioList){
                LibraryAudiosDto dto = new LibraryAudiosDto(la.getAudioBook().getBook(), la.getAudioBook());
                libraryAudiosDtoList.add(dto);
            }
            return libraryAudiosDtoList;
    }


    //서재 불러오기 1-2. 내 서재에 담긴 책 목록 > 목록에서 책 삭제
    @Transactional
    public Long removeLibraryBook (Long bookId, User user){
        Books book = booksService.findBook(bookId);
        try {
//            Library_Books library_books = library_booksRepository.findByUserLibraryAndBook(user.getUserLibrary(), book);
            Library_Books library_books = library_booksRepository.findByBookAndUserLibrary(book, user.getUserLibrary());
            library_booksRepository.delete(library_books);
        }catch (NullPointerException e){
            e.getMessage();
        }
       return bookId;
    }

    //서재 불러오기 2-2. 내가 듣고 있는 오디오북 > 목록에서 오디오북 삭제
    @Transactional
    public Long removeLibraryAudio(Long audioId, User user){
        AudioBook audioBook = audioService.findAudioBook(audioId);
        try{
            Library_Audio library_audio = library_audioRepository.findByUserLibraryAndAudioBook(user.getUserLibrary(), audioBook);
            library_audioRepository.delete(library_audio);
        } catch (NullPointerException e){
           e.getMessage();
        }
        return audioId;
    }


    //마이페이지 조회 .3-1 판매자 전용 버튼, 내가 등록한 오디오북
    @Transactional
    public List<SellerAudioBook> sellerMyAudioBook(User user){

        List<AudioBook> audioBookList = user.getAudioBookList();
        List<SellerAudioBook> sellerAudioBookList = new ArrayList<>();
        int i = 0;   //최신순으로 뽑아주기 위한 반복문.
        for (i = audioBookList.size() ; i > 0; i--){
            SellerAudioBook sa = new SellerAudioBook(audioBookList.get(i-1));
            sellerAudioBookList.add(sa);
        }
//        for(AudioBook a : audioBookList){
//            SellerAudioBook sa = new SellerAudioBook(a);
//            sellerAudioBookList.add(sa);
//        }
        return sellerAudioBookList;
    }

    //마이페이지 조회 .3-2 판매자 전용 버튼, 내가 등록한 펀딩 목록
    public List<SellerFundDto> myFund(User seller){
        List<Fund> myFundList = fundRepository.findAllByUserIdOrderByFundIdDesc(seller.getId());
        List<SellerFundDto> sellerFundDtoList = new ArrayList<>();
        for(Fund f : myFundList) {
            SellerFundDto dto = new SellerFundDto(f);
            sellerFundDtoList.add(dto);
        }
        return sellerFundDtoList;
    }


    //사용자 프로필 등록
    public UserProfileDto newProfile(MultipartFile file, String email){
        User user = userService.findUser(email);

        String imageName = pathImage + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        String s3Path = awsS3Service.fileUpload(bucket, file, imageName);
        // 이미지 파일만 올릴 수 있게 예외처리하는 코드 추가해주자!!!!

        user.getUserProfile().editProfile(s3Path, imageName);
        userProfileRepository.save(user.getUserProfile());
        return new UserProfileDto(user.getUserProfile());
    }

    //판매자 프로필 등록
    public SellerProfileDto.ResponseDto newProfileSeller(MultipartFile file, String email, SellerProfileDto dto){
        User user = userService.findUser(email);

        String imageName = pathImage + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        String s3Path = awsS3Service.fileUpload(bucket, file, imageName);

        user.getUserProfile().editSellerProfile(s3Path, imageName, dto.getIntroduce());
        userProfileRepository.save(user.getUserProfile());
        // 이미지 파일만 올릴 수 있게 처리하는 코드 추가해주자!!!!
        return new SellerProfileDto.ResponseDto(user.getUserProfile());
    }


    //todo:프로필 수정
//    public void editProfile (MultipartFile file, String email, SellerProfileDto dto) {
//        User user = userService.findUser(email);
//
//        if (user.getRole() == UserRole.USER){
//            awsS3Service.removeImage();
//        }
//
//    }

    //todo: 판매자 음성 등록 및 수정
    @Transactional
    public void sellerMyVoice(MultipartFile file, User user){
        UserProfile profile = user.getUserProfile();
        if (profile.getS3FileName()!=null) {  //S3에 파일이 삭제가 안됨 ㅡ,.ㅡ
            awsS3Service.removeS3File(profile.getS3FileName());
        }
        Map<String, String> fileName = awsS3Service.uploadFile(file, pathInfo);
        profile.addMyVoice(fileName.get("url"), fileName.get("fileName"));
        userProfileRepository.save(profile);
    }

}
