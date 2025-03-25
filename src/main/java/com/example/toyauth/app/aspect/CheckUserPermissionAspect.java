package com.example.toyauth.app.aspect;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.common.annotation.CheckUserOwn;
import com.example.toyauth.app.common.annotation.CheckUserOwnOrAdmin;
import com.example.toyauth.app.common.enumuration.Role;
import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserPermissionAspect {

    @Before("@annotation(checkUserOwn) && args(userId,..)")
    public void checkUserOwn(Long userId, CheckUserOwn checkUserOwn) {
        Long currentUserId = getCurrentUserId();

        if (!userId.equals(currentUserId)) {
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
        }
    }

    @Before("@annotation(checkUserOwnOrAdmin) && args(userId,..)")
    public void checkUserOwnOrAdmin(Long userId, CheckUserOwnOrAdmin checkUserOwnOrAdmin) {
        Long currentUserId = getCurrentUserId();
        boolean isAdmin = hasRole(Role.ADMIN.getValue());

        if (!isAdmin && !userId.equals(currentUserId)) {
            throw new RestApiException(UserErrorCode.UNAUTHORIZED);
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId(); // 사용자 정의 UserDetails의 ID 필드
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}

