package com.example.missiontshoppingmall.user;

import com.example.missiontshoppingmall.utils.EntityFromOptional;
import com.example.missiontshoppingmall.utils.S3FileService;
import com.example.missiontshoppingmall.user.dto.request.BARequest;
import com.example.missiontshoppingmall.user.dto.request.UserAdditionalInfoDto;
import com.example.missiontshoppingmall.user.dto.request.UserRegisterDto;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import com.example.missiontshoppingmall.user.dto.response.AdditionalInfo;
import com.example.missiontshoppingmall.user.dto.response.BAResult;
import com.example.missiontshoppingmall.user.dto.response.UserRegisterResponse;
import com.example.missiontshoppingmall.user.entity.CustomUserDetails;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.jwt.JwtRequestDto;
import com.example.missiontshoppingmall.user.jwt.JwtResponseDto;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomUserDetailsManager manager;
    private final EntityFromOptional optional;
    private final S3FileService s3FileService;

    // 유저 로그인
    public JwtResponseDto userLogin(JwtRequestDto dto) {
        return manager.userLogin(dto);
    }

    // 유저 회원가입
    @Transactional
    public UserRegisterResponse userRegister(UserRegisterDto dto) {
        //dto에서 UserDetails로 변환
        CustomUserDetails inactiveUser = CustomUserDetails.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .authorities("ROLE_INACTIVE")
                .build();

        log.info(inactiveUser.toString());
        manager.createUser(inactiveUser);
        return UserRegisterResponse.builder()
                .userId(inactiveUser.getUserId())
                .build();
    }

    // 추가 정보 입력 (인증관련 정보가 아니므로 그냥 UserService에서 메서드 구현하였다.)
    @Transactional
    public AdditionalInfo additionalInfo(String accountId, UserAdditionalInfoDto dto) {

        UserEntity foundUser = optional.getFoundUser(accountId);
        // 로그인한 아이디와 이 유저가 같은지 확인
        manager.checkIdIsEqual(foundUser.getAccountId());

        // 하나라도 null이면 false 반환, 모두 null이 아니면 true 반환
        boolean dtoCheck = Stream.of(
                        dto.getAge(), dto.getEmail(), dto.getUsername(), dto.getNickname(), dto.getPhone())
                .allMatch(Objects::nonNull);
        if (dtoCheck) { // true면 정보 업데이트 + ACTIVE USER로 권한설정
            foundUser.setUsername(dto.getUsername());
            foundUser.setEmail(dto.getEmail());
            foundUser.setNickname(dto.getNickname());
            foundUser.setPhone(dto.getPhone());
            foundUser.setAge(dto.getAge());
            foundUser.setAuthority("ROLE_ACTIVE");
            foundUser = userRepository.save(foundUser);
        }
        // true면 업데이트된 정보를, 아닐경우는 본래정보 리턴
        AdditionalInfo response = AdditionalInfo.fromEntity(foundUser);
        return response;
    }

    // 프로필 사진 추가
    @Transactional
    public String uploadProfileImage(String accountId, List<MultipartFile> multipartFile) {
        // 본인인지 확인
        manager.checkIdIsEqual(accountId);
        // 해당 유저 엔티티의 profile이 null이 아니면 해당 파일을 삭제하고, 새로운 프로필 사진을 업로드 할 것.

        // 1. 해당 유저 엔티티 갖고오기
        UserEntity foundUser = optional.getFoundUser(accountId);
        // 2. profile이 null인지 확인
        if (foundUser.getProfile() != null) {
            // null이 아니면 해당 url에 있는 사진객체를 삭제할 것
            s3FileService.deleteImage("/profile", foundUser.getProfile()
                    .substring(foundUser.getProfile().lastIndexOf("/")+1,
                            foundUser.getProfile().length()-1));
        }

        // s3에 해당 프로필 사진 업로드 (단 한장만 가능!!)
        if (multipartFile.size() > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필 사진은 한장만 업로드 가능");
        }
        List<String> uploadUrls =  s3FileService.uploadIntoS3("/profile", multipartFile);
        log.info("uploadUrl  "+uploadUrls.toString());

        foundUser.setProfile(uploadUrls.toString());
        userRepository.save(foundUser);
        return foundUser.getProfile();
    }

    // 사업자계정 전환 신청
    @Transactional
    public BAResponse registerBA (String accountId, BARequest dto) {
        UserEntity foundUser = optional.getFoundUser(accountId);
        // 로그인한 아이디와 이 유저가 같은지 확인
        manager.checkIdIsEqual(foundUser.getAccountId());

        // 등록요청 update
        foundUser.setBusinessNumber(dto.getBusinessNumber());
        foundUser.setBusinessIsAllowed(false);
        foundUser = userRepository.save(foundUser);

        return BAResponse.fromEntity(foundUser);
    }

    //사업자계정 등록결과 확인
    public BAResult baResultCheck(String accountId) {
        UserEntity foundUser = optional.getFoundUser(accountId);
        // 로그인한 아이디와 이 유저가 같은지 확인
        manager.checkIdIsEqual(foundUser.getAccountId());
        return BAResult.fromEntity(foundUser);
    }

}
