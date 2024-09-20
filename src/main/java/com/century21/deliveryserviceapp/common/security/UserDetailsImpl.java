package com.century21.deliveryserviceapp.common.security;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한을 반환합니다. (USER 또는 OWNER 권한을 반환)
        return Collections.singleton(() -> "ROLE_" + user.getAuthority().name());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 사용자의 이메일을 username으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부를 확인하는 메서드 (만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부를 확인하는 메서드 (잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보의 만료 여부 (만료되지 않음)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

    public Long getUserId() {
        return user.getId(); // 사용자 ID 반환
    }

    public String getNickname() {
        return user.getNickname(); // 사용자 닉네임 반환
    }
}