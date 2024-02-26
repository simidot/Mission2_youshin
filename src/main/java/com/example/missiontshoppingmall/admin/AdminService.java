package com.example.missiontshoppingmall.admin;

import com.example.missiontshoppingmall.admin.dto.BAManagement;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    // 관리자의 사업자 전환신청 거절/수락 기능
    public BAResponse manageBusinessAccount(String accountId, BAManagement agreement) {
        // 계정id로 찾고,
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();
        // 신청자는 active상태
        String authorities = "ROLE_ACTIVE";
        // 관리자가 승인한 경우
        if (agreement.isBusinessAllowance()) {
            // 권한과, 사업자허가여부 속성을 변경&update
            authorities = "ROLE_BUSINESS";
            foundUser.setBusinessIsAllowed(true);
            foundUser.setAuthority(authorities);
            userRepository.save(foundUser);
        }
        // 승인이 거절된 경우는 본래 UserEntity에 저장된 결과로 반환

        // 반환타입을 맞춰 return
        return BAResponse.builder()
                .accountId(accountId)
                .authority(authorities)
                .businessNumber(foundUser.getBusinessNumber())
                .businessIsAllowed(foundUser.getBusinessIsAllowed())
                .build();
    }

    // 사업자 전환신청 전체조회
    public List<BAResponse> findAllBARequests() {
        List<UserEntity> userEntityList = userRepository.findByBusinessNumberIsNotNullOrderByUpdatedAtDesc();
        List<BAResponse> responseList = new ArrayList<>();
        for (UserEntity entity : userEntityList) {
            responseList.add(BAResponse.fromEntity(entity));
        }
        return responseList;
    }

    // 사업자 전환신청 단일조회
    public BAResponse findOneBARequest(String accountId) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();
        return BAResponse.fromEntity(foundUser);
    }

}
