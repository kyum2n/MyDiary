package com.example.mydiary.service.impl;

import com.example.mydiary.entity.Member;
import com.example.mydiary.repository.MemberMapper;
import com.example.mydiary.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper; // ✅ MyBatis 매퍼로 변경

    // 로그인 시 사용자 아이디(uId)로 회원 조회
    @Override
    public UserDetails loadUserByUsername(String uId) throws UsernameNotFoundException {
        Member member = memberMapper.findByUid(uId); // ✅ null 리턴 기반으로 처리

        if (member == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + uId);
        }

        return new MyUserDetails(member); // ✅ UserDetails 구현체로 변환
    }
}