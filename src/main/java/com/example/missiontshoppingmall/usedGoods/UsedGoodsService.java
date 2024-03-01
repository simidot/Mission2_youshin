package com.example.missiontshoppingmall.usedGoods;

import com.example.missiontshoppingmall.utils.EntityFromOptional;
import com.example.missiontshoppingmall.utils.S3FileService;
import com.example.missiontshoppingmall.usedGoods.dto.request.UsedGoodsDto;
import com.example.missiontshoppingmall.usedGoods.dto.response.UsedGoodsWithoutSeller;
import com.example.missiontshoppingmall.usedGoods.entity.SaleStatus;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoodsImage;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsImageRepo;
import com.example.missiontshoppingmall.usedGoods.repo.UsedGoodsRepo;
import com.example.missiontshoppingmall.user.CustomUserDetailsManager;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsedGoodsService {
    private final UsedGoodsRepo usedGoodsRepo;
    private final CustomUserDetailsManager manager;
    private final EntityFromOptional optional;
    private final S3FileService s3FileService;
    private final UsedGoodsImageRepo imageRepo;


    // 중고물품 업로드
    public UsedGoodsDto uploadUsedGoods(List<MultipartFile> multipartFile, UsedGoodsDto dto) {
        // todo: 고민인 것! authentication 정보 넘겨받아서 하는게 맞나!?
        //  인증정보관련은 CustomUSerDetails에서 모두 처리하는 것이 나을까?

        // 1. 셀러 정보 인증정보에서 가져오기
        UserEntity seller = manager.loadUserFromAuth();

        // 2. seller정보와 dto를 가지고와서 새로운 UsedGoods 객체 생성
        UsedGoods newGoods = UsedGoods.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .minimumPrice(dto.getMinimumPrice())
                .imageList(new ArrayList<>())
                .suggestionList(new ArrayList<>())
                .saleStatus(SaleStatus.ON_SALE)
                .seller(seller) // 판매자는 저장은 하지만, 실제로 조회시에는 보이지 않아야한다.
                .build();
        newGoods = usedGoodsRepo.save(newGoods);

        // 3. S3에 이미지들을 업로드하고, 그 파일들이 저장된 url을 String으로 반환
        List<String> urls = s3FileService.uploadIntoS3("/usedgoods", multipartFile);
        // 4. 이 String List 하나하나 UsedGoodsImage 엔티티가 각각 생긴다.
        for (String url : urls) {
            UsedGoodsImage image = UsedGoodsImage.builder()
                    .imgUrl(url)
                    .build();
            image.addUsedGoods(newGoods);
            imageRepo.save(image);
        }
        return UsedGoodsDto.fromEntity(newGoods);
    }

    // 중고거래 등록된 물품 전체조회 (판매자 정보는 보이지 않아야 한다)
    public List<UsedGoodsWithoutSeller> readAllGoods() {
        List<UsedGoodsWithoutSeller> response = new ArrayList<>();
        List<UsedGoods> usedGoods = usedGoodsRepo.findAll();
        for (UsedGoods g : usedGoods) {
            response.add(UsedGoodsWithoutSeller.fromEntity(g));
        }
        return response;
    }

    // 중고거래 등록된 물품 단일조회
    public UsedGoodsWithoutSeller readOneGoods(Long usedGoodsId) {
        UsedGoods foundGoods = optional.getUsedGoods(usedGoodsId);
        return UsedGoodsWithoutSeller.fromEntity(foundGoods);
    }

    // 중고거래 등록 물품 수정
    @Transactional
    public UsedGoodsDto updateUsedGoods(List<MultipartFile> multipartFile, Long usedGoodsId, UsedGoodsDto dto) {
        // 1. 중고거래 물품 id로 물품 찾기
        UsedGoods foundGoods = optional.getUsedGoods(usedGoodsId);

        // 2. 로그인한 아이디와 물품 등록 판매자가 다르면 unauthorized
        manager.checkIdIsEqual(foundGoods.getSeller().getAccountId());

        // 3. 수정사항 반영
        foundGoods.setTitle(dto.getTitle());
        foundGoods.setDescription(dto.getDescription());
        foundGoods.setMinimumPrice(dto.getMinimumPrice());
        foundGoods = usedGoodsRepo.save(foundGoods);

        // 4. 사진이 추가되면 새로운 Image 엔티티가 생성된다.
        List<String> urls = s3FileService.uploadIntoS3("/usedgoods", multipartFile);
        for (String url : urls) {
            UsedGoodsImage image = UsedGoodsImage.builder()
                    .imgUrl(url)
                    .build();
            image.addUsedGoods(foundGoods);
            imageRepo.save(image);
        }
        return UsedGoodsDto.fromEntity(foundGoods);
    }

    // 중고거래 등록한 물품 삭제
    @Transactional
    public void deleteUsedGoods(Long usedGoodsId) {
        // 1. id로 물품 찾고,
        UsedGoods foundGoods = optional.getUsedGoods(usedGoodsId);
        // 2. 판매자인지 확인하고,
        manager.checkIdIsEqual(foundGoods.getSeller().getAccountId());
        // 중고물품 엔티티에서 삭제. -> 이때 함께 연관된 usedGoodsImage도 삭제되기 때문에!!
        // 3. 그전에 S3에서 삭제를 해주기로 한다.
        for (UsedGoodsImage image : foundGoods.getImageList()) {
            s3FileService.deleteImage("/usedgoods", image.getImgUrl());
        }
        // 4. foundGoods 삭제
        usedGoodsRepo.delete(foundGoods);
    }
}
