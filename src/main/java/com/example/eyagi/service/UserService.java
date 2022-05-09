package com.example.eyagi.service;


import com.example.eyagi.dto.SignupRequestDto;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserLibrary;
import com.example.eyagi.model.UserProfile;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.repository.UserLibraryRepository;
import com.example.eyagi.repository.UserProfileRepository;
import com.example.eyagi.repository.UserRepository;
import com.example.eyagi.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final UserLibraryRepository libraryRepository;

    public User findUser (String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new NullPointerException("등록되지 않은 사용자 입니다.")
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
        User user = new User(email, username, enPassword, role);
        userRepository.save(user);
        UserLibrary userLibrary = new UserLibrary(user);
        UserProfile userProfile = new UserProfile(user);
        libraryRepository.save(userLibrary);
        profileRepository.save(userProfile);
        return ResponseEntity.ok().body("회원가입 완료");
    }

//    @Transactional
//    public ResponseEntity<UserDto.MypageDto> viewMyPage(UserDetailsImpl userDetails) {
//        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
//                () -> new NullPointerException("회원정보가 존재하지 않습니다.")
//        );
//        UserDto.MypageDto mypageDto = UserDto.MypageDto.builder()
//                .email(userDetails.getUsername())
//                .username(user.getUsername())
////                .userimgurl(user.getUserImage())
////                .userimgname(user.getOriginImage())
//                .build();
//        return ResponseEntity.ok().body(mypageDto);
//    }

    //팔로우 팔로잉
//    public void addFollow(String users, Long userId) {
//        String username = jwtDecoder.decodeUsername(users);
//        User followerUser = userRepository.findFollowUserByEmail(username);
//
//        User user = userRepository.findUserById(userId)
//                .orElseThrow(() -> new NullPointerException("무야호"));
//
//        //객체지향적으로 추가(연관관계의 주인으로도 삽입)
//        followerUser.getUserFollowing().getFollowingList().add(user);
//        followerUser.getFollowingList().add(user);
//
//        user.getUserFollower().getFollowerList().add(followerUser);
//        user.getFollowerList().add(followerUser);
//    }

    // 유저의 pk 값으로 유저 조회
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("회원이 아닙니다."));
    }

}
