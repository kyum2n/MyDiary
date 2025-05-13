package com.example.mydiary.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.mydiary.entity.Member;

import lombok.Getter;

@Getter
public class MyUserDetails implements UserDetails, OAuth2User {
    // public class MyUserDetails implements UserDetails {

    private final Member member;

    public MyUserDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return member.getUPwd();
    }

    @Override
    public String getUsername() {
        return member.getUId(); // 로그인 ID 기준
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

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.singletonMap("uId", member.getUId()); // 또는 필요한 attribute들
    }

    @Override
    public String getName() {
        return member.getUId(); // 고유 키
    }
}