package com.example.missiontshoppingmall.utils;

import com.example.missiontshoppingmall.user.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFacade {
    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
