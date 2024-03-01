package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.usedGoods.dto.request.UsedGoodsDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.UsedGoodsWithoutSeller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/used-goods")
public class UsedGoodsController {
    private final UsedGoodsService usedGoodsService;

    // 물품등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UsedGoodsDto registerUsedGoods(
            @RequestPart("file") List<MultipartFile> multipartFile,
            @RequestPart UsedGoodsDto dto
    ) {
        return usedGoodsService.uploadUsedGoods(multipartFile, dto);
    }

    // 중고거래 등록된 물품 전체조회
    @GetMapping
    public List<UsedGoodsWithoutSeller> readAllGoods() {
        return usedGoodsService.readAllGoods();
    }

    // 중고거래 등록된 물품 단일조회
    @GetMapping("/{usedGoodsId}")
    public UsedGoodsWithoutSeller readOneGoods(
            @PathVariable("usedGoodsId") Long usedGoodsId
    ) {
        return usedGoodsService.readOneGoods(usedGoodsId);
    }

    // 중고거래 등록 물품 수정
    @PutMapping("/{usedGoodsId}")
    public UsedGoodsDto updateUsedGoods(
            @PathVariable("usedGoodsId") Long id,
            @RequestPart("file") List<MultipartFile> multipartFile,
            @RequestPart UsedGoodsDto dto
    ) {
        return usedGoodsService.updateUsedGoods(multipartFile, id, dto);
    }

    // 중고거래 등록한 물품 삭제
    @DeleteMapping("/{usedGoodsId}")
    public String deleteUsedGoods(
            @PathVariable("usedGoodsId") Long id
    ) {
        usedGoodsService.deleteUsedGoods(id);
        return "중고물품 삭제 완료";
    }

}
