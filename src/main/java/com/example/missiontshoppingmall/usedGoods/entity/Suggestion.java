package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Suggestion extends BaseEntity {
    @Column(nullable = false)
    private String suggestionMessage;

    @Enumerated(EnumType.STRING)
    @Setter
    private SuggestionStatus suggestionStatus;

    @Enumerated(EnumType.STRING)
    @Setter
    private PurchaseStatus purchaseStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private UsedGoods usedGoods;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity buyer; //buyer
}
