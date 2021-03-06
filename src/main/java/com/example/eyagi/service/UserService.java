package com.example.eyagi.service;


import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.dto.TodayCreatorDto;
import com.example.eyagi.dto.UserDto;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserLibrary;
import com.example.eyagi.model.UserProfile;
import com.example.eyagi.model.UserRole;

import com.example.eyagi.repository.QRepository.UserCustomRepositiry;
import com.example.eyagi.repository.UserLibraryRepository;
import com.example.eyagi.repository.UserProfileRepository;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.security.UserDetailsImpl;
import com.example.eyagi.security.jwt.JwtDecoder;
import com.example.eyagi.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;
    private final UserProfileRepository profileRepository;
    private final UserLibraryRepository libraryRepository;


    public User findUserId (Long id){
        return userRepository.findById(id).orElseThrow(
                ()-> new NullPointerException("등록되지 않은 사용자입니다.")
        );
    }
    public User findUser (String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new NullPointerException("등록되지 않은 사용자입니다.")
        );
    }
    public User findUsername (String userName) {
        return userRepository.findByUsername(userName).orElseThrow(
                ()-> new NullPointerException("등록되지 않은 사용자입니다.")
        );
    }
    //이메일 중복체크
    public String userEmailCheck(String email){
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()){
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
        return email;
    }

    //닉네임 중복체크
    public String userNameCheck(String username){
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()){
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }
        return username;
    }

    //회원가입 요청시, 중복체크 및 확인.
    public ResponseEntity<String> registerUser(SignupRequestDto signupRequestDto) {
        Optional<User> found = userRepository.findByEmail(signupRequestDto.getEmail());
        UserValidator.checkUser(found, signupRequestDto);
        // userId, username
        String email = signupRequestDto.getEmail();
        String username = signupRequestDto.getUsername();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
        userNameCheck(username); //닉네임 중복체크
        // 패스워드 암호화
        String enPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        // 유저권한
        UserRole role = UserRole.USER;

        // 유저 생성 후 DB 저장
        UserLibrary userLibrary = new UserLibrary();
        UserProfile userProfile = new UserProfile();
        libraryRepository.save(userLibrary);
        profileRepository.save(userProfile);
        User user = new User(email, username, enPassword, role);
        user.newLibrary(userLibrary);
        user.newProfile(userProfile);
        userRepository.save(user);
        return ResponseEntity.ok().body("회원가입 완료");
    }

    @Transactional
    public ResponseEntity<UserDto.MypageDto> viewMyPage(UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new NullPointerException("회원정보가 존재하지 않습니다.")
        );
        UserDto.MypageDto mypageDto = UserDto.MypageDto.builder()
                .email(userDetails.getUsername())
                .username(user.getUsername())
                .build();
        return ResponseEntity.ok().body(mypageDto);
    }

    @Transactional
    public List<TodayCreatorDto> showMainCreator() {

        List<User> userList = userRepository.findByRole(UserRole.SELLER);
        Collections.shuffle(userList);

        List<TodayCreatorDto> todayCreatorDtoList= new ArrayList<>();

        for (User u : userList) {
            try {
                String image = u.getUserProfile().getUserImage();
                TodayCreatorDto todayCreator = TodayCreatorDto.builder()
                        .id(u.getId())
                        .email(u.getEmail())
                        .userImage(image)
                        .username(u.getUsername())
                        .build();
                todayCreatorDtoList.add(todayCreator);
            } catch (NullPointerException e) {
                TodayCreatorDto todayCreator = TodayCreatorDto.builder()
                        .id(u.getId())
                        .email(u.getEmail())
                        .username(u.getUsername())
                        .build();
                todayCreatorDtoList.add(todayCreator);
            }
        }

        //creator가 6명보다 적다면 null값 넣기
        if(todayCreatorDtoList.size() < 5){
            for(int i = todayCreatorDtoList.size(); i < 5; i++)
            todayCreatorDtoList.add(null);
        }
        List<TodayCreatorDto>todayCreatorList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
                todayCreatorList.add(todayCreatorDtoList.get(i));
        }

        return todayCreatorList;
    }

    @Transactional
    public Page<UserCustomRepositiry> findSellerList (Pageable pageable){
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt" );
        pageable = PageRequest.of(page, pageable.getPageSize(), sort);
        Page<UserCustomRepositiry> sellerList = userRepository.findByOrderByRole(UserRole.SELLER, pageable);
        return sellerList;
    }

}