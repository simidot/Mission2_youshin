package com.example.missiontshoppingmall.user.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import com.example.missiontshoppingmall.order.entity.ItemOrder;
import com.example.missiontshoppingmall.shoppingMall.entity.ShoppingMall;
import com.example.missiontshoppingmall.usedGoods.entity.Suggestion;
import com.example.missiontshoppingmall.usedGoods.entity.UsedGoods;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
//todo: Setter를 써야할지말지 고민이 된다. > 일단 accountId는 변경이 불가하기 때문에 변경이 가능한 필드에만 Setter를 써주었다.
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String accountId;
    @Column(nullable = false)
    @Setter
    private String password;
    @Setter
    private String authority; //권한을 ,로 구분지어 String으로 저장.

    //추가 입력 정보
    @Setter
    private String username;
    @Setter
    private String nickname;
    @Setter
    private Integer age;
    @Setter
    private String email;
    @Setter
    private String phone;

    //선택 입력 사항
    @Setter
    private String profile; //이미지 저장 경로를 저장 (이미지 자체를 저장하지 않음)
    @Setter
    private String businessNumber; //사업자 번호
    @Setter
    private Boolean businessIsAllowed; //사업자 허가 여부

    // 연관관계
    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<UsedGoods> sellingList;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Suggestion> suggestionList;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<ShoppingMall> shoppingMallList;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<ItemOrder> orderList;
}
