package com.example.missiontshoppingmall.admin;

import com.example.missiontshoppingmall.admin.dto.BAManagement;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    // 관리자의 사업자 전환신청 거절/수락 기능
    @PostMapping("/business/{accountId}")
    public BAResponse manageBusinessAccount(
            @PathVariable("accountId") String accountId,
            @RequestBody BAManagement agreement
    ) {
        return adminService.manageBusinessAccount(accountId, agreement);
    }
}
