package com.example.missiontshoppingmall.shoppingMall;

import com.example.missiontshoppingmall.EntityFromOptional;
import com.example.missiontshoppingmall.admin.dto.MallOpenResult;
import com.example.missiontshoppingmall.shoppingMall.dto.MallCloseRequest;
import com.example.missiontshoppingmall.shoppingMall.dto.MallCloseResponse;
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

    // 쇼핑몰 개설 신청
    public MallOpenResponse createOpenRequest(Long mallId, MallOpenRequest dto) {
        // 해당 사용자가 Business인지 확인
        UserEntity foundUser= manager.loadUserFromAuth();
        if (!foundUser.getAuthority().equals("ROLE_BUSINESS")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        // 해당 사용자가 쇼핑몰 주인인지 확인
        ShoppingMall foundMall = optional.getMall(mallId);
        manager.checkIdIsEqual(foundMall.getOwner().getAccountId());

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

    // 쇼핑몰 정보수정 (주인만 가능)
    public MallOpenResponse updateMallInfo(Long mallId, MallOpenRequest dto) {
        ShoppingMall foundMall = optional.getMall(mallId);
        // 이 쇼핑몰의 주인의 계정으로 로그인되어 있는지 확인. (다르면 예외처리)
        manager.checkIdIsEqual(foundMall.getOwner().getAccountId());
        foundMall.setName(dto.getName());
        foundMall.setDescription(dto.getDescription());
        foundMall.setLargeCategory(dto.getLargeCategory());
        mallRepo.save(foundMall);
        return MallOpenResponse.fromEntity(foundMall);
    }

    // 쇼핑몰 폐쇄요청 (주인만 가능)
    public MallCloseResponse closeRequest(Long mallId, MallCloseRequest dto) {
        ShoppingMall foundMall = optional.getMall(mallId);
        // 이 쇼핑몰의 주인인지 확인 (다르면 예외처리)
        manager.checkIdIsEqual(foundMall.getOwner().getAccountId());

        //요청 유형을 close로 바꾸고, 이유를 넣는다
        foundMall.setRequestType(dto.getRequestType());
        foundMall.setCloseReason(dto.getCloseReason());
        mallRepo.save(foundMall);
        return MallCloseResponse.fromEntity(foundMall);
    }
}
