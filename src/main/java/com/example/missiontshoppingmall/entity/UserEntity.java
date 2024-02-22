package com.example.missiontshoppingmall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
//todo: Setter를 써야할지말지 고민이 된다.
public class UserEntity extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false)
    private String password;
    private String authority; //권한을 ,로 구분지어 String으로 저장.

    //추가 입력 정보
    private String username;
    private String nickname;
    private Integer age;
    private String email;
    private String phone;

    //선택 입력 사항
    private String profile; //이미지 저장 경로를 저장 (이미지 자체를 저장하지 않음)
    private String businessNumber; //사업자 번호
    private String businessIsAllowed; //사업자 허가 여부


}
