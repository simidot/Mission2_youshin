package com.example.missiontshoppingmall.admin.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallOpenManagement {
    private boolean allowed;
    private String deniedReason;
}
