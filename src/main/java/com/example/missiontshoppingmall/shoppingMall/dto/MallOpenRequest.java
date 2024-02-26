package com.example.missiontshoppingmall.shoppingMall.dto;

import com.example.missiontshoppingmall.shoppingMall.entity.LargeCategory;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallOpenRequest {
    private String name;
    private String description;
    private LargeCategory largeCategory;
}
