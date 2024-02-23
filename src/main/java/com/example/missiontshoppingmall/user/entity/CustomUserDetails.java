package com.example.missiontshoppingmall.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private String userId;
    private String password;
    private String authorities; //권한을 ,로 구분지어 String으로 저장.

    //todo: 추가정보/선택정보를 UserDetails에 넣는것이 맞는가?
//    //추가 입력 정보
//    private String username;
//    private String nickname;
//    private Integer age;
//    private String email;
//    private String phone;
//
//    //선택 입력 사항
//    private String profile; //이미지 저장 경로를 저장 (이미지 자체를 저장하지 않음)
//    private String businessNumber; //사업자 번호
//    private String businessIsAllowed; //사업자 허가 여부

    public String getStringAuthorities() {
        return this.authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(", ")).sorted()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
