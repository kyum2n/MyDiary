package com.example.mydiary.security;

import com.example.mydiary.entity.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class MyUserDetails implements UserDetails {

    private final Member member; // 로그인한 사용자 정보

    // 생성자: Member 객체를 받아서 내부에 저장
    public MyUserDetails(Member member) {
        this.member = member;
    }

    // 사용자 권한 설정 (현재는 권한 없음)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    // 사용자 비밀번호 반환 (로그인 검증용)
    @Override
    public String getPassword() {
        return member.getUPwd();
    }

    // 사용자 이름(아이디) 반환
    @Override
    public String getUsername() {
        return member.getUId();
    }

    // 계정 만료 여부 (true면 사용 가능)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부 (true면 사용 가능)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부 (true면 사용 가능)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 (true면 사용 가능)
    @Override
    public boolean isEnabled() {
        return true;
    }
}