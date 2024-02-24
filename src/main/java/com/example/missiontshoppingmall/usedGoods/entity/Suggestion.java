package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Suggestion extends BaseEntity {
    @Column(nullable = false)
    private String suggestionMessage;

    @Enumerated(EnumType.STRING)
    private SuggestionStatus suggestionStatus;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsedGoods usedProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity buyer; //buyer
}
