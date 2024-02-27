package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.*;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallOpenResponse {
    private Long id;
    private String name;
    private String description;
    private LargeCategory largeCategory;
    //쇼핑몰 운영상태
    private RunningStatus runningStatus;
    //요청종류
    private RequestType requestType;
    //개설요청 허가여부
    private Allowance allowance;

    public static MallOpenResponse fromEntity(ShoppingMall entity) {
        return MallOpenResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .largeCategory(entity.getLargeCategory())
                .runningStatus(entity.getRunningStatus())
                .requestType(entity.getRequestType())
                .allowance(entity.getAllowance())
                .build();
    }
}
