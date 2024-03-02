package com.example.missiontshoppingmall.item.dto;

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
    private Integer stock;
    private Integer price;
}
