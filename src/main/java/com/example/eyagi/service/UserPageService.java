package com.example.eyagi.service;

import com.example.eyagi.dto.BooksDto;
import com.example.eyagi.dto.SellerProfileDto;
import com.example.eyagi.dto.UserLibraryAudiosDto;
import com.example.eyagi.dto.UserProfileDto;
import com.example.eyagi.model.*;
import com.example.eyagi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //듣고 있는 오디오북에 추가
    public void listenBook (AudioBook audioBook, User user){

        UserLibrary library = userLibraryRepository.findByUserId(user.getId());
        if (library == null){
            UserLibrary userLibrary = new UserLibrary(user);
            Library_Audio library_audio = new Library_Audio(userLibrary,audioBook);
            userLibrary.addAuidoBook(library_audio);
            userLibraryRepository.save(userLibrary);
            user.addLibrary(userLibrary);
            library_audioRepository.save(library_audio);
        } else {
            Library_Audio library_audio = new Library_Audio(library,audioBook);
            library.addAuidoBook(library_audio);
            library_audioRepository.save(library_audio);
            userLibraryRepository.save(library);
        }
    }

    //내 서재에 책 담기
    public void heartBook(String email, Long id){
        User user = userService.findUser(email);
        Books books = booksService.findBook(id);
        UserLibrary library = userLibraryRepository.findByUserId(user.getId());
        if (library == null){
            UserLibrary userLibrary = new UserLibrary(user);
            Library_Books library_books = new Library_Books(userLibrary, books);
            userLibrary.addBook(library_books);
            userLibraryRepository.save(userLibrary);
            user.addLibrary(userLibrary);
            library_booksRepository.save(library_books);

        } else {
            Library_Books library_books = new Library_Books(library, books);
            library.addBook(library_books);
            library_booksRepository.save(library_books);
            userLibraryRepository.save(library);
        }
    }

    //todo:서재 불러오기 1. 내 서재에 담긴 책 목록
    public List<BooksDto> loadMyLibraryBooks(User user){
        List<Library_Books> library_booksList = user.getUserLibrary().getMyBook(); //담은 도서.
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
    //todo:서재 불러오기 2. 내가 듣고 있는 오디오북
    public void loadMyLibraryAudios(User user){
//        List<Library_Audio> library_audioList = user.getUserLibrary().getMyAuidoBook(); //듣고 있는 오디오북.
//        List<UserLibraryAudiosDto> libraryAudiosDtoList = new ArrayList<>();
//        for(Library_Audio la : library_audioList){
//            UserLibraryAudiosDto dto = new UserLibraryAudiosDto();
//
//        }
    }


    //사용자 프로필 등록
    public UserProfileDto newProfile(MultipartFile file, String email){
        User user = userService.findUser(email);

        String imageName = "Image" + "/" + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        String s3Path = awsS3Service.fileUpload(bucket, file, imageName);
        // 이미지 파일만 올릴 수 있게 예외처리하는 코드 추가해주자!!!!

        UserProfile userProfile = UserProfile.builder()
                .originImage(imageName)
                .userImage(s3Path)
                .user(user)
                .build();
        userProfileRepository.save(userProfile);

        return new UserProfileDto(userProfile);
    }

    //판매자 프로필 등록
    public SellerProfileDto.ResponseDto newProfileSeller(MultipartFile file, String email, SellerProfileDto dto){
        User user = userService.findUser(email);

        String imageName = "Image" + "/" + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        String s3Path = awsS3Service.fileUpload(bucket, file, imageName);

        UserProfile userProfile = UserProfile.builder()
                    .originImage(imageName)
                    .userImage(s3Path)
                    .introduce(dto.getIntroduce())
                    .user(user)
                    .build();
            userProfileRepository.save(userProfile);

        // 이미지 파일만 올릴 수 있게 처리하는 코드 추가해주자!!!!
        return new SellerProfileDto.ResponseDto(userProfile);
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



}
