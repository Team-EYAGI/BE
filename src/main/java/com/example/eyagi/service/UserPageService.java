package com.example.eyagi.service;

import com.example.eyagi.dto.SellerProfileDto;
import com.example.eyagi.dto.UserProfileDto;
import com.example.eyagi.model.User;
import com.example.eyagi.model.UserProfile;
import com.example.eyagi.model.UserRole;
import com.example.eyagi.repository.UserProfileRepository;
import com.example.eyagi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserPageService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final UserService userService;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


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
        // 이미지 파일만 올릴 수 있게 예외처리하는 코드 추가해주자!!!!

        UserProfile userProfile = UserProfile.builder()
                .originImage(imageName)
                .userImage(s3Path)
                .introduce(dto.getIntroduce())
                .user(user)
                .build();
        userProfileRepository.save(userProfile);

        return new SellerProfileDto.ResponseDto(userProfile);
    }


    //프로필 수정
    public void editProfile (MultipartFile file, String email, SellerProfileDto dto) {
        User user = userService.findUser(email);

        if (user.getRole()== UserRole.USER){

        }

    }


}
