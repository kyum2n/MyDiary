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

    @Override
    public UserDetails loadUserByUsername(String uId) throws UsernameNotFoundException {
        Member member = memberRepository.findByuId(uId)
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + uId));
        return new MyUserDetails(member);
    }
}