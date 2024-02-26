package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.shoppingMall.dto.MallOpenRequest;
import com.example.missiontshoppingmall.shoppingMall.dto.MallOpenResponse;
import com.example.missiontshoppingmall.shoppingMall.entity.RequestType;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.shoppingMall.repo.ShoppingMallRepo;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShoppingMallService {
    private final ShoppingMallRepo mallRepo;
    private final CustomUserDetailsManager manager;
    private final EntityFromOptional optional;

    public MallOpenResponse createOpenRequest(MallOpenRequest dto) {
        // 해당 사용자가 Business인지 확인
        UserEntity foundUser= manager.loadUserFromAuth();
        log.info("foundUser"+foundUser.getAccountId());
        if (!foundUser.getAuthority().equals("ROLE_BUSINESS")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // dto 확인
        // 하나라도 null이면 false 반환, 모두 null이 아니면 true 반환
        boolean dtoCheck = Stream.of(dto.getName(), dto.getDescription(), dto.getLargeCategory())
                .allMatch(Objects::nonNull);
        // 해당 사용자의 shoppingmall을 찾아오기
        log.info("mall Id: "+foundUser.getShoppingMall().getId());
        ShoppingMall mall = optional.getMall(foundUser.getShoppingMall().getId());
        if (dtoCheck) {
            mall.setName(dto.getName());
            mall.setDescription(dto.getDescription());
            mall.setLargeCategory(dto.getLargeCategory());
            mall.setRequestType(RequestType.OPEN);
            mallRepo.save(mall);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return MallOpenResponse.fromEntity(mall);
    }
}
