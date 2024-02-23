package com.example.missiontshoppingmall.config;

import com.example.missiontshoppingmall.user.jwt.JwtTokenFilter;
import com.example.missiontshoppingmall.user.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {
        httpSecurity
                //csrf 보안 해제
                .csrf(AbstractHttpConfigurer::disable)
                //url에 따른 요청 인가
                .authorizeHttpRequests(
                        auth -> //로그인, 회원가입은 익명사용자만 요청 가능
                                auth.requestMatchers("/users/login", "/users/register")
                                        .anonymous()
                                        // 추가정보 입력은 inactive만 가능
                                        .requestMatchers("/users/additional-info", "/users/business")
                                        .hasAnyRole("INACTIVE", "ACTIVE")
                                        // 중고거래, 쇼핑몰 서비스 이용 active, business만 가능
                                        .requestMatchers("/used-goods")
                                        .hasAnyRole("ACTIVE")
                                        // 쇼핑몰 운영 서비스는 business회원만 가능
                                        .requestMatchers("/shopping-mall")
                                        .hasRole("BUSINESS")
                                        // 관리자 페이지는 관리자만 가능
                                        .requestMatchers("/admin/**")
                                        .hasAnyRole("ADMIN")
                                        .anyRequest().authenticated()

                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtils, manager), AuthorizationFilter.class);
        return httpSecurity.build();
    }
}
