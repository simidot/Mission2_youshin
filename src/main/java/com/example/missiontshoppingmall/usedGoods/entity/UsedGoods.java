package com.example.missiontshoppingmall.usedGoods.entity;

import com.example.missiontshoppingmall.BaseEntity;
import com.example.missiontshoppingmall.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Setter
    private String imageUrl;

    @Column(nullable = false)
    @Setter
    private Integer minimumPrice;

    @Enumerated(EnumType.STRING)
    @Setter
    private SaleStatus saleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity seller; //seller

    @OneToMany(mappedBy = "usedGoods")
    private List<Suggestion> suggestionList = new ArrayList<>();

}
