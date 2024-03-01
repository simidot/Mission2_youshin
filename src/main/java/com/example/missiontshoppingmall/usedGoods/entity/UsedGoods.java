package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.utils.BaseEntity;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UsedGoods extends BaseEntity {
    @Column(nullable = false)
    @Setter
    private String title;

    @Lob
    @Setter
    private String description;

    @Column(nullable = false)
    @Setter
    private Integer minimumPrice;

    @Enumerated(EnumType.STRING)
    @Setter
    private SaleStatus saleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity seller; //seller

    @OneToMany(mappedBy = "usedGoods", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suggestion> suggestionList = new ArrayList<>();

    @OneToMany(mappedBy = "usedGoods", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsedGoodsImage> imageList = new ArrayList<>();
}
