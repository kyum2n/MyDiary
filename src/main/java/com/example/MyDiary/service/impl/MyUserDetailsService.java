package com.example.mydiary.service.impl;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberRepository;
import com.example.mydiary.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

        // 로그인 시 사용자 아이디(uId)로 회원 조회
    @Override
    public UserDetails loadUserByUsername(String uId) throws UsernameNotFoundException {
        // uId로 회원 검색, 없으면 예외 발생
        Member member = memberRepository.findByuId(uId)
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + uId));
            // Member -> UserDetails 변환
        return new MyUserDetails(member);
    }
}