package com.example.missiontshoppingmall.admin;

import com.example.missiontshoppingmall.admin.dto.*;
import com.example.missiontshoppingmall.shoppingMall.dto.MallCloseResponse;
import com.example.missiontshoppingmall.shoppingMall.dto.MallOpenResponse;
import com.example.missiontshoppingmall.user.dto.response.BAResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    // 관리자의 사업자 전환신청 거절/수락 기능
    @PostMapping("/business/{accountId:.*}")
    public BAResponse manageBusinessAccount(
            @PathVariable("accountId") String accountId,
            @RequestBody BAManagement agreement
    ) {
        return adminService.manageBusinessAccount(accountId, agreement);
    }

    // 사업자 전환신청 리스트 조회
    @GetMapping("/business")
    public List<BAResponse> baRequests() {
        return adminService.findAllBARequests();
    }

    // 사업자 전환신청 단일조회
    @GetMapping("/business/{accountId}")
    public BAResponse baRequest(
            @PathVariable("accountId") String accountId
    ) {
        return adminService.findOneBARequest(accountId);
    }

    // 개설신청된 쇼핑몰 목록 전체조회
    @GetMapping("/shopping-malls/open-requests")
    public List<MallOpenResponse> allMallOpenRequests() {
        return adminService.readAllOpenRequest();
    }

    // 개설신청된 쇼핑몰 목록 단일조회
    @GetMapping("/shopping-malls/open-requests/{mallId}")
    public MallOpenResponse oneMallOpenRequest(
            @PathVariable("mallId") Long id
    ) {
        return adminService.readOneOpenRequest(id);
    }

    // 개설신청된 쇼핑몰 허가/불허
    @PostMapping("/shopping-malls/open-requests/{mallId}/allowance")
    public MallOpenResult allowMallOpen(
            @PathVariable("mallId") Long id,
            @RequestBody MallManagement management
    ) {
        return adminService.allowOpenOrNot(id, management);
    }

    // 폐쇄신청된 쇼핑몰 목록 전체조회
    @GetMapping("/shopping-malls/close-requests")
    public List<MallCloseResponse> allMallCloseRequests() {
        return adminService.readAllCloseRequest();
    }

    // 폐쇄신청된 쇼핑몰 목록 단일조회
    @GetMapping("/shopping-malls/close-requests/{mallId}")
    public MallCloseResponse oneMallCloseRequest(
            @PathVariable("mallId") Long id
    ) {
        return adminService.readOneCloseRequest(id);
    }

    // 폐쇄신청된 쇼핑몰 수락/거절
    @PostMapping("/shopping-malls/close-requests/{mallId}/allowance")
    public MallCloseResult allowMallClose(
            @PathVariable("mallId") Long id,
            @RequestBody MallManagement management
    ) {
        return adminService.allowCloseOrNot(id, management);
    }
}
