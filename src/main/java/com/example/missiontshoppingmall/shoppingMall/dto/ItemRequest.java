package com.example.missiontshoppingmall.shoppingMall.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemRequest {
    private String name;
    private String image;
    private String description;
    private String mediumCategory;
    private String smallCategory;
    private Integer stock;
    private Integer price;
}
