package com.example.missiontshoppingmall.admin.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAManagement { //관리자의 사업자계정 전환 허락 요청
    // 수락 true, 거절 false
    private boolean businessAllowance;
}
