package com.example.missiontshoppingmall.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//들어온 요청에 대해 토큰 유효성 확인 &이에 따른 인증정보를 등록해주는 필터
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. authorization 헤더를 회수
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 2. 헤더가 존재하는지, Bearer로 시작하는지
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 3. 존재하면 유효한 토큰인지 확인
            String token = authHeader.split(" ")[1];
            //토큰 확인
            if (jwtTokenUtils.validate(token)) {
                // contextHolder에서 context를 만들고,
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // 토큰에서 사용자 정보 가져오기
                String username = jwtTokenUtils.parseClaims(token).getSubject();
                log.info("username:: "+username);

                UserDetails userDetails = manager.loadUserByUsername(username);
                log.info(userDetails.toString());
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    log.info("authority: {}", authority.getAuthority());
                }

                //인증 정보 생성
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, token, userDetails.getAuthorities()
                );
                //인증 정보 등록
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
                log.info("===set security context with jwt");
                log.info("Auth :: "+SecurityContextHolder.getContext().getAuthentication().toString());
                //TODO: 위 로그는 지울 것
            } else {
                log.warn("jwt validation failed!");
            }
        }
        filterChain.doFilter(request, response);
    }
}
