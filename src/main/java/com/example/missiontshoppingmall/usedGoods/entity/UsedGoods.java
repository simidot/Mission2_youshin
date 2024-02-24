package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UsedGoods extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private Integer minimumPrice;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity seller; //seller

    @OneToMany(mappedBy = "usedProduct")
    private List<Suggestion> suggestionList = new ArrayList<>();

}
