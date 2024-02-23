package com.example.missiontshoppingmall.admin;

import com.example.missiontshoppingmall.admin.dto.BAManagement;
import com.example.missiontshoppingmall.user.dto.client.BAResponse;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.example.missiontshoppingmall.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public BAResponse manageBusinessAccount(String accountId, BAManagement agreement) {
        Optional<UserEntity> optionalUser = userRepository.findByAccountId(accountId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        UserEntity foundUser = optionalUser.get();
        String authorities = "USER_ACTIVE";
        if (agreement.isBusinessAllowance()) {
            authorities = "USER_ACTIVE, USER_BUSINESS";
            foundUser.setBusinessIsAllowed(true);
            foundUser.setAuthority(authorities);
            userRepository.save(foundUser);
        }
        BAResponse response = BAResponse.builder()
                .accountId(accountId)
                .authority(authorities)
                .businessNumber(foundUser.getBusinessNumber())
                .businessIsAllowed(foundUser.getBusinessIsAllowed())
                .build();
        return response;
    }

}
