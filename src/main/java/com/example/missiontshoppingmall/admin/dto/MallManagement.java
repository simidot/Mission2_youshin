package com.example.missiontshoppingmall.admin.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MallManagement {
    private boolean allowed;
    private String deniedReason;
}
