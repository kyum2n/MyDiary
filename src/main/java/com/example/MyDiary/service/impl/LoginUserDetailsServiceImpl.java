package com.example.mydiary.service.impl;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByuId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        return new User(
                member.getUId(),  // 로그인 식별자로 uId 사용
                member.getUPwd(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}