package com.example.missiontshoppingmall.user.dto.client;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BAResponse {
    private String accountId;
    private String authority;
    private String businessNumber;
    private boolean businessIsAllowed;
}
